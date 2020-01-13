package com.example.emag.utils;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.model.dto.LoginUserDTO;
import com.example.emag.model.dto.RegisterUserDTO;
import com.example.emag.model.dto.UserPasswordDTO;
import com.example.emag.model.dto.UserWithoutPasswordDTO;
import com.example.emag.model.pojo.Address;

public class UserUtil {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    public static final String FIRST_NAME_PATTERN = "([A-Z][a-zA-Z]*).{2,45}";
    public static final String LAST_NAME_PATTERN = "([a-zA-z]+([ '-][a-zA-Z]+)*).{2,45}";
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    public static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,225})";
    public static final String CITY_PATTERN = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    public static final String DISTRICT_PATTERN = "^[0-9a-zA-Z\\s,-]+$";
    public static final String STREET_PATTERN = "^[#.0-9a-zA-Z\\s,-]+$";
    public static final String ZIP_PATTERN = "^[0-9\\-\\+]{4,4}$";
    public static final String PHONE_NUMBER_PATTERN = "^[0-9\\-\\+]{6,15}$";

    public static boolean isFirstNameValid(String firstName){
        return firstName.matches(FIRST_NAME_PATTERN);
    }

    public static boolean isLastNameValid(String lastName){
        return lastName.matches(LAST_NAME_PATTERN);
    }

    public static boolean isEMailValid(String email){
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isPasswordValid(String password){
        return password.matches(PASSWORD_PATTERN);
    }

    public static boolean isCityValid(String city) {
        return city.matches(CITY_PATTERN);
    }

    public static boolean isDistrictValid(String district) {
        return district.matches(DISTRICT_PATTERN);
    }

    public static boolean isStreetValid(String street) {
        return street.matches(STREET_PATTERN);
    }

    public static boolean isZipValid(String ZIP) {
        return ZIP.matches(ZIP_PATTERN);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    public static void trimSpacesRegisterUser(RegisterUserDTO registerUserDTO) {
        registerUserDTO.setFirst_name(registerUserDTO.getFirst_name().trim());
        registerUserDTO.setLast_name(registerUserDTO.getLast_name().trim());
        registerUserDTO.setEMail(registerUserDTO.getEMail().trim());
        registerUserDTO.setPassword(registerUserDTO.getPassword().trim());
        registerUserDTO.setConfirmPassword(registerUserDTO.getConfirmPassword().trim());
    }

    public static void validateRegisterDTO(RegisterUserDTO registerUserDto) {
        trimSpacesRegisterUser(registerUserDto);
        if (!isFirstNameValid(registerUserDto.getFirst_name())) {
            throw new BadRequestException("First name should not be empty and should contains at least 2 characters " +
                    "and starts with uppercase letter!");
        }
        if (!isLastNameValid(registerUserDto.getLast_name())) {
            throw new BadRequestException("Last name should not be empty and should contains at least 2 characters!");
        }
        if (!isEMailValid(registerUserDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
        if (!isPasswordValid(registerUserDto.getPassword())) {
            throw new BadRequestException("Password should not be empty and should contains at least 6 characters, " +
                    "at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!isPasswordValid(registerUserDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password should not be empty and should contains at " +
                    "least 6 characters, at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!registerUserDto.getPassword().trim().equals(registerUserDto.getConfirmPassword().trim())) {
            throw new BadRequestException("Password and confirm password does not match each other!");
        }
    }

    public static void trimSpacesLogin(LoginUserDTO loginUserDto) {
        loginUserDto.setEMail(loginUserDto.getEMail().trim());
        loginUserDto.setPassword(loginUserDto.getPassword().trim());
    }

    public static void validateLogin(LoginUserDTO loginUserDto) {
        trimSpacesLogin(loginUserDto);
        if (!isEMailValid(loginUserDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
    }

    public static void trimSpacesEditProfile(UserWithoutPasswordDTO userWithoutPasswordDto) {
        userWithoutPasswordDto.setFirst_name(userWithoutPasswordDto.getFirst_name().trim());
        userWithoutPasswordDto.setLast_name(userWithoutPasswordDto.getLast_name().trim());
        userWithoutPasswordDto.setEMail(userWithoutPasswordDto.getEMail().trim());
    }

    public static void validateEditProfile(UserWithoutPasswordDTO userWithoutPasswordDto) {
        trimSpacesEditProfile(userWithoutPasswordDto);
        if (!isFirstNameValid(userWithoutPasswordDto.getFirst_name())) {
            throw new BadRequestException("First name should not be empty and should contains at least 2 characters " +
                    "and starts with uppercase letter!");
        }
        if (!isLastNameValid(userWithoutPasswordDto.getLast_name())) {
            throw new BadRequestException("Last name should not be empty and should contains at least 2 characters!");
        }
        if (!isEMailValid(userWithoutPasswordDto.getEMail())) {
            throw new BadRequestException("Invalid e-mail!");
        }
    }

    public static void trimSpacesChangePassword(UserPasswordDTO userPasswordDto) {
        userPasswordDto.setOldPassword(userPasswordDto.getOldPassword().trim());
        userPasswordDto.setNewPassword(userPasswordDto.getNewPassword().trim());
        userPasswordDto.setConfirmPassword(userPasswordDto.getConfirmPassword().trim());
    }

    public static void validateChangePassword(UserPasswordDTO userPasswordDto) {
        trimSpacesChangePassword(userPasswordDto);
        if (!isPasswordValid(userPasswordDto.getNewPassword())) {
            throw new BadRequestException("New password should not be empty and should contains at least 6 characters, " +
                    "at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
        if (!isPasswordValid(userPasswordDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password should not be empty and should contains " +
                    "at least 6 characters, at least 1 uppercase letter," +
                    " at least 1 lowercase letter, at least 1 number (0 to 9) and 1 special symbol (@  #  $  %  !");
        }
    }

    public static void trimSpacesAddress(Address address) {
        address.setCity(address.getCity().trim());
        address.setDistrict(address.getDistrict().trim());
        address.setStreet(address.getStreet().trim());
        address.setZip(address.getZip().trim());
        address.setPhoneNumber(address.getPhoneNumber().trim());
    }

    public static void validateAddress(Address address) {
        trimSpacesAddress(address);
        if (!UserUtil.isCityValid(address.getCity())) {
            throw new BadRequestException("City should not be empty!");
        }
        if (!UserUtil.isDistrictValid(address.getDistrict())) {
            throw new BadRequestException("District should not be empty!");
        }
        if (!UserUtil.isStreetValid(address.getStreet())) {
            throw new BadRequestException("Street should not be empty!");
        }
        if (!UserUtil.isZipValid(address.getZip())) {
            throw new BadRequestException("ZIP invalid!");
        }
        if (!UserUtil.isPhoneNumberValid(address.getPhoneNumber())) {
            throw new BadRequestException("Phone number invalid!");
        }
    }
}
