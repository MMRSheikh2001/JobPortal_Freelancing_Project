export interface DistrictRequestModel {
    districtName: string;
    divisionId: number;
}

export interface DistrictResponseModel {
    districtId: number;
    districtName: string;
    divisionId: number;
    divisionName: number;

    countryId: number;
    countryName: string;
    countryCode: string
}