package com.lunarlog.service.impl;

import com.lunarlog.dto.response.ShipmentResponseDTO;
import com.lunarlog.exception.ResourceNotFoundException;
import com.lunarlog.mapper.*;
import com.lunarlog.util.*;
import com.lunarlog.model.Shipment;
import com.lunarlog.model.ShipmentStatusHistory;
import com.lunarlog.repository.ShipmentRepository;
import com.lunarlog.repository.ShipmentStatusHistoryRepository;
import com.lunarlog.service.ShipmentService;
import lombok.RequiredArgsConstructor;

import com.lunarlog.dto.request.*;
import com.lunarlog.model.*;
import com.lunarlog.model.enums.ShipmentStatus;
import com.lunarlog.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

	private final ShipmentRepository shipmentRepository;
	private final ShipmentStatusHistoryRepository shipmentHistoryRepository;
	private final ParticipantRepository participantRepository;
	private final LocationRepository locationRepository;
	private final PackageRepository packageRepository;
	private final ShipmentTypeRepository shipmentTypeRepository;
	private final EmployeeRepository employeeRepository;

	@Override
	public ShipmentResponseDTO findByTrackingCode(String trackingCode) {
		Shipment shipment = findEntityByTrackingCode(trackingCode);
		ShipmentResponseDTO dto = ShipmentMapper.toDTO(shipment);

		List<ShipmentStatusHistory> history = findHistoryEntityByShipmentId(shipment.getId());

		return new ShipmentResponseDTO(
				dto.id(),
				dto.trackingCode(),
				dto.keyword(),
				dto.senderId(),
				dto.receiverId(),
				dto.packageId(),
				dto.shipmentType(),
				dto.originLocationId(),
				dto.destinationLocationId(),
				dto.freightValue(),
				dto.deliveryPersonId(),
				dto.currentStatus(),
				dto.isDeleted(),
				dto.shipmentDate(),
				dto.completionDate(),
				dto.updatedAt(),
				history);
	}

	@Override
	public Shipment findEntityByTrackingCode(String trackingCode) {
		return shipmentRepository.findByTrackingCode(trackingCode)
				.orElseThrow(
						() -> new ResourceNotFoundException("Shipment not found with tracking code: " + trackingCode));
	}

	@Override
	public List<ShipmentStatusHistory> findHistoryEntityByShipmentId(Long shipmentId) {
		return shipmentHistoryRepository.findByShipmentId(shipmentId);
	}

	@Override
	@Transactional
	public ShipmentResponseDTO createShipment(ShipmentRequestDTO dto) {
		// Busca ou cria localização de origem
		Location originLocation = locationRepository.findByCepAndNumber(
				dto.originLocation().cep(),
				dto.originLocation().number())
				.map(existingLocation -> {
					// Atualiza dados da localização existente
					existingLocation.setStreet(dto.originLocation().street());
					existingLocation.setComplement(dto.originLocation().complement());
					existingLocation.setNeighborhood(dto.originLocation().neighborhood());
					existingLocation.setCity(dto.originLocation().city());
					existingLocation.setState(dto.originLocation().state());
					return locationRepository.save(existingLocation);
				})
				.orElseGet(() -> locationRepository.save(
						LocationMapper.toEntity(dto.originLocation())));

		// Busca ou cria localização de destino
		Location destinationLocation = locationRepository.findByCepAndNumber(
				dto.destinationLocation().cep(),
				dto.destinationLocation().number())
				.map(existingLocation -> {
					// Atualiza dados da localização existente
					existingLocation.setStreet(dto.destinationLocation().street());
					existingLocation.setComplement(dto.destinationLocation().complement());
					existingLocation.setNeighborhood(dto.destinationLocation().neighborhood());
					existingLocation.setCity(dto.destinationLocation().city());
					existingLocation.setState(dto.destinationLocation().state());
					return locationRepository.save(existingLocation);
				})
				.orElseGet(() -> locationRepository.save(
						LocationMapper.toEntity(dto.destinationLocation())));

		// Busca ou cria remetente
		Participant sender = participantRepository.findByCpf(dto.sender().cpf())
				.map(existingSender -> {
					existingSender.setFullName(dto.sender().fullName());
					existingSender.setEmail(dto.sender().email());
					existingSender.setPhone(dto.sender().phone());
					existingSender.setLocation(originLocation);
					return participantRepository.save(existingSender);
				})
				.orElseGet(() -> participantRepository.save(
						ParticipantMapper.toEntity(dto.sender(), originLocation)));

		// Busca ou cria destinatário
		Participant receiver = participantRepository.findByCpf(dto.receiver().cpf())
				.map(existingReceiver -> {
					existingReceiver.setFullName(dto.receiver().fullName());
					existingReceiver.setEmail(dto.receiver().email());
					existingReceiver.setPhone(dto.receiver().phone());
					existingReceiver.setLocation(destinationLocation);
					return participantRepository.save(existingReceiver);
				})
				.orElseGet(() -> participantRepository.save(
						ParticipantMapper.toEntity(dto.receiver(), destinationLocation)));

		// Cria pacote (sem restrição de unicidade aparente)
		PackageEntity packageEntity = packageRepository.save(PackageMapper.toEntity(dto.packageEntity()));

		ShipmentType shipmentType = shipmentTypeRepository.findByName(dto.shipmentType())
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de envio não encontrado"));

		Employee deliveryPerson = null;
		if (dto.deliveryPersonId() != null) {
			deliveryPerson = employeeRepository.findById(dto.deliveryPersonId())
					.orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado"));
		}

		// Gera código de rastreamento único
		String trackingCode;
		Optional<Shipment> existingShipment;
		do {
			trackingCode = CodeGenerator.generateTrackingCode();
			existingShipment = shipmentRepository.findByTrackingCode(trackingCode);
		} while (existingShipment.isPresent());

		String keyword = CodeGenerator.generateRandomKeyword(8);

		Shipment shipment = new Shipment();
		shipment.setTrackingCode(trackingCode);
		shipment.setKeyword(keyword);
		shipment.setSender(sender);
		shipment.setReceiver(receiver);
		shipment.setPackageEntity(packageEntity);
		shipment.setShipmentType(shipmentType);
		shipment.setOriginLocation(originLocation);
		shipment.setDestinationLocation(destinationLocation);
		shipment.setFreightValue(dto.freightValue());
		shipment.setDeliveryPerson(deliveryPerson);
		shipment.setCurrentStatus(ShipmentStatus.REGISTERED);
		shipment.setShipmentDate(OffsetDateTime.now());

		shipment = shipmentRepository.save(shipment);

		ShipmentStatusHistory history = ShipmentStatusHistory.builder()
				.shipmentId(shipment.getId())
				.status(shipment.getCurrentStatus())
				.location(sender.getLocation())
				.nextLocation(null)
				.build();

		shipmentHistoryRepository.save(history);

		return ShipmentMapper.toDTO(shipment);
	}

}
