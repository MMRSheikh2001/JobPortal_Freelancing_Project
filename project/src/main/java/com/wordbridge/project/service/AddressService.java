package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.AddressRequestDTO;
import com.wordbridge.project.dto.responsedto.AddressResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    AddressResponseDTO save(AddressRequestDTO a);

    List<AddressResponseDTO> getAll();

    AddressResponseDTO findById(Long id);

    void delete(Long id);

    AddressResponseDTO update(Long id, AddressRequestDTO a);


    //Response DTO methods

    List<AddressResponseDTO> findByCountryId(Long id);

    List<AddressResponseDTO> findByCountryName(String name);


    List<AddressResponseDTO> findByDivisionId(Long id);

    List<AddressResponseDTO> findByDivisionName(String name);

    List<AddressResponseDTO> findByDistrictId(Long id);

    List<AddressResponseDTO> findByDistrictName(String name);

    List<AddressResponseDTO> findByPoliceStationId(Long id);

    List<AddressResponseDTO> findByPoliceStationName(String name);

}
