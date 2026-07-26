export interface DivisionRequestModel {
    divisionName: string;
    countryId: number;
}

export interface DivisionResponseModel {
    divisionId: number;
    divisionName: string;

    countryId: number;
    countryName: string;
    countryCode: string;


}