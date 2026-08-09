import { UserRole } from "../../enums/user-role.enum";

export interface UserResponseDTO {
    id: number;
    email: string;
    name: string;
    role: UserRole;
    isVerified: boolean;
    isActive: boolean;
    isSuspended: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface UserSearchRequestDTO {

    keyword?: string;

    role?: UserRole;

    isVerified?: boolean;

    isActive?: boolean;

    isSuspended?: boolean;

}