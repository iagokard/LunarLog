export interface Location {
	city: string;
	state: string;
	complement: string;
}

export interface StatusHistory {
	status: string;
	location: Location;
	nextLocation?: Location | null;
	eventTime: string;
}

export interface Shipment {
	trackingCode: string;
	shipmentType: string;
	currentStatus: string;
	statusHistory: StatusHistory[];
}

export interface ApiError {
	status: number;
	error: string;
	message: string;
	path?: string;
	timestamp?: string;
}

export interface Address {
	name: string;
	cpf: string;
	email: string;
	phone: string;
	cep: string;
	street: string;
	number: string;
	complement?: string;
	neighborhood: string;
	city: string;
	state: string;
}

export interface Dimensions {
	weight: number | null;
	height: number | null;
	width: number | null;
	length: number | null;
}

export interface FreightResponse {
	value: number;
	estimatedDelivery: string;
}
