package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.Operation;

import java.util.Collection;

public interface OperationService {
    Collection<Operation> getOperationsByAccountId(int idAccount);
}
