package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.OperationDAO;
import fr.pantheonsorbonne.ufr27.miage.model.Operation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class OperationServiceImpl implements OperationService{
    @Inject
    OperationDAO operationDAO;

    @Override
    public Collection<Operation> getOperationsByAccountId(int idAccount) {
        return operationDAO.getOperationsForAccount(idAccount);
    }


}
