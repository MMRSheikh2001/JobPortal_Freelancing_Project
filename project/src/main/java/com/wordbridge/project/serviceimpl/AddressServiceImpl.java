package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.AddressMapper;
import com.wordbridge.project.dto.requestdto.AddressRequestDTO;
import com.wordbridge.project.dto.responsedto.AddressResponseDTO;
import com.wordbridge.project.entity.*;
import com.wordbridge.project.repository.*;
import com.wordbridge.project.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;


    private final AddressMapper addressMapper;


    @Override
    public AddressResponseDTO save(AddressRequestDTO a) {
        Address address = addressMapper.toEntity(a);
        Address saved = addressRepository.save(address);
        return addressMapper.toDTO(saved);
    }

    @Override
    public List<AddressResponseDTO> getAll() {
        return addressRepository.findAll().stream().map(addressMapper::toDTO).toList();

    }

    @Override
    public AddressResponseDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("No Address Found"));

        return addressMapper.toDTO(address);
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressResponseDTO update(Long id, AddressRequestDTO a) {
        Address address = addressMapper.toEntity(a);
        address.setId(id);
        Address updated = addressRepository.save(address);
        return addressMapper.toDTO(updated);
    }

    //Address Response DTO Implementation Services
    @Override
    public List<AddressResponseDTO> findByCountryId(Long id) {
        List<Address> list = addressRepository.findByPoliceStationDistrictDivisionCountryId(id);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByCountryName(String name) {
        List<Address> list = addressRepository.findByPoliceStationDistrictDivisionCountryName(name);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByDivisionId(Long id) {
        List<Address> list = addressRepository.findByPoliceStationDistrictDivisionId(id);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByDivisionName(String name) {
        List<Address> list = addressRepository.findByPoliceStationDistrictDivisionName(name);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByDistrictId(Long id) {
        List<Address> list = addressRepository.findByPoliceStationDistrictId(id);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByDistrictName(String name) {
        List<Address> list = addressRepository.findByPoliceStationDistrictName(name);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByPoliceStationId(Long id) {
        List<Address> list = addressRepository.findByPoliceStationId(id);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AddressResponseDTO> findByPoliceStationName(String name) {
        List<Address> list = addressRepository.findByPoliceStationName(name);
        return list.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

}
