package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Transaction;

import java.util.List;

public interface TransactionDAO {

    Transaction findTransactionsByAccountId(int id_transaction);

}