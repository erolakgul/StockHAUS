package com.erolakgul.stockhaus.service.sqlite;

import android.util.Log;

import com.erolakgul.stockhaus.repository.dataaccess.Repository;
import com.erolakgul.stockhaus.repository.dataaccess.groupRepository;
import com.erolakgul.stockhaus.repository.dataaccess.inventoryRepository;
import com.erolakgul.stockhaus.repository.dataaccess.materialRepository;
import com.erolakgul.stockhaus.repository.dataaccess.storeRepository;
import com.erolakgul.stockhaus.repository.dataaccess.userRepository;

public class servicePoints {

    private static servicePoints singleton;

    private static userRepository _userRepository;
    private static materialRepository _materialRepository;
    private static inventoryRepository _inventoryRepository;
    private static storeRepository _storeRepository;
    private static groupRepository _groupRepository;
    private static Repository _repository;

    private servicePoints() {

    }

    // turns just one instance
    public static servicePoints getInstance() {
        return singleton = (singleton == null) ? new servicePoints() : singleton;
    }

    public userRepository get_userRepository() {
        return _userRepository = (_userRepository == null) ? userRepository.getInstance() : _userRepository;
        // return userRepository.getInstance();
    }

    public materialRepository get_materialRepository() {
        return _materialRepository = (_materialRepository == null) ? materialRepository.getInstance() : _materialRepository;
    }

    public inventoryRepository get_inventoriesRepository() {
        return _inventoryRepository = (_inventoryRepository == null) ? inventoryRepository.getInstance() : _inventoryRepository;
    }

    public groupRepository get_groupRepository() {
        return _groupRepository = (_groupRepository == null) ? groupRepository.getInstance() : _groupRepository;
    }

    public storeRepository get_storeRepository(){
        //return _storeRepository = (_storeRepository == null) ? storeRepository.getInstance() : _storeRepository;
        return storeRepository.getInstance();
    }

    public Repository get_Repository() {
        return _repository = (_repository == null) ? Repository.getInstance() : _repository;
    }

}
