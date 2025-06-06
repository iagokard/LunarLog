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
