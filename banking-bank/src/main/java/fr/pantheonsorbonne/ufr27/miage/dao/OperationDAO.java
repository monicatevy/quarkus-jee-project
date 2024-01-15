package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Operation;

import java.util.Collection;
import java.util.List;

public interface OperationDAO {
    Collection<Operation> getOperationsForAccount(int idAccount);
}
