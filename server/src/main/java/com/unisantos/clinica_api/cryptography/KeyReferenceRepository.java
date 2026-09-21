package com.unisantos.clinica_api.cryptography;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyReferenceRepository extends JpaRepository<KeyReference, Integer> {}
