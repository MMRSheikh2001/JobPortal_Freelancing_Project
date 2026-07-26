
export interface CompanyProfileRequestModel {
    userId: number;
    name: string;
    phone: string;
    companyEmail: string;
    companyDescription: string;
    companyWebsite: string;
    industry: string;
    foundedYear: string;
    tradeLicenseNumber: string;
    locationId: number;
    locationDetails: string;
    locationPostCode: string;
    locationPoliceStationId: number;
}



export interface CompanyProfileResponseModel {
    id: number;
    userId: number;
    userEmail: string;
    name: string;
    phone: string;
    companyEmail: string;
    image: string;
    companyDescription: string;
    companyWebsite: string;
    industry: string;
    foundedYear: string;
    tradeLicenseNumber: string;
    createdAt: string;
    updatedAt: string;
    locationId: number;
    locationDetails: string;
    locationPostCode: string;
    locationCountryId: number;
    locationCountryName: string;
    locationCountryCode: string;
    locationDivisionId: number;
    locationDivisionName: string;
    locationDistrictId: number;
    locationDistrictName: string;
    locationPoliceStationId: number;
    locationPoliceStationName: string;
}

export interface CompanySearchRequestDTO {
    keyword?: string;
    industry?: string;
    countryId?: number;
    divisionId?: number;
    districtId?: number;
}