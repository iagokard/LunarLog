export interface ShipmentType {
	id: number;
	name: string;
	freightPerWeight: number;
	freightPerDistance: number;
	freightPerVolume: number;
	maxWeightExemption: number;
	maxDistanceExemption: number;
	maxVolumeExemption: number;
}

export interface TomTomGeocodeResult {
	summary: {
		numResults: number;
	};
	results: {
		position: {
			lat: number;
			lon: number;
		};
		address: {
			freeformAddress: string;
		};
	}[];
}

export interface TomTomRouteResult {
	routes: {
		summary: {
			lengthInMeters: number;
			travelTimeInSeconds: number;
		};
	}[];
}

export interface FreightCalculation {
	value: number;
	estimatedDelivery: string;
}

export interface ApiError {
	status: number;
	error: string;
	message: string;
	path?: string;
	timestamp?: string;
}
