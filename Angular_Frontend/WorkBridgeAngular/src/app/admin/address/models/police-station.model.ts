export interface PoliceStationRequestModel {
    policeStationName: string;
    districtId: number;
}

export interface PoliceStationResponseModel {
    policeStationId: number;
    policeStationName: string;
    districtId: number;
    districtName: string;

    divisionId: number;
    divisionName: string;

    countryId: number;
    countryName: string;
    countryCode: string
}