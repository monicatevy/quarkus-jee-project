package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.AccountDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.CustomerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.NotificationDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.BankAccountNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.BankCustomerNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.NotificationFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.NotificationNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.Account;
import fr.pantheonsorbonne.ufr27.miage.model.Customer;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    NotificationDAO notificationDAO;
    @Mock
    CustomerDAO customerDAO;
    @Mock
    AccountDAO accountDAO;
    @Mock
    CompteService compteService;
    @InjectMocks
    NotificationServiceImpl notificationService;
    private Calendar cal = Calendar.getInstance();
    private java.util.Date currentDate = new java.util.Date();
    @BeforeEach
    public void setup(){

        cal.setTime(currentDate);

        //updateNotification
        lenient().when(notificationDAO.findById(eq(1)))
                .thenReturn(new Notification("Demande de synchronisation de compte",
                        Constante.VALID,
                        1,
                        new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR))
                        ,"Authorisation"));

    }
    @Test
    void notificationAuthorisationAvailableForAnAccount() throws NotificationNotFoundException {
        //Date of today
        Notification notificationAvailable = new Notification("Demande de synchronisation valide",Constante.VALID,1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),"Authorisation");
        //Date of yesterday
        Notification notificationNotAvailable = new Notification("Demande de synchronisation invalide",Constante.VALID,1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1899,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),"Authorisation");
        notificationNotAvailable.setIdNotification(2);
        Collection<Notification> notificationsList = new ArrayList<>();
        notificationsList.add(notificationAvailable);
        notificationsList.add(notificationNotAvailable);

        lenient().when(notificationDAO.findNotificationAuthorisationAvailableForAccount(1))
                .thenReturn(notificationsList);

        List<Notification> notificationAvailableTest =(List<Notification>) notificationService.notificationAuthorizationAvailableForAnAccount(1);

        assertEquals("Demande de synchronisation valide",notificationAvailableTest.get(0).getTexte());
    }
    @Test
    void notificationNotFound() throws NotificationNotFoundException {

        lenient().doThrow(NotificationNotFoundException.class)
                .when(notificationDAO).findNotificationAuthorisationAvailableForAccount(eq(1));

        assertEquals(null,notificationService.notificationAuthorizationAvailableForAnAccount(1));

    }
//method verifyNotificationCreated
    @Test
    void verifyNotificationNotCreated() throws NotificationFoundException.NotificationAuthorisationFoundException {
        Account a = new Account("pass",0.0,1);
        a.setIdAccount(1);
        User user = new User("a@gmail.com","pass");
        DemandeAuthorisation demandeAuthorisation = new DemandeAuthorisation(user,"Demande de synchronisation V2");

        Notification n = new Notification("Demande de synchronisation V1",Constante.VALID,1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),"Authorisation");

        Collection<Notification> notificationsList = new ArrayList<>();
        notificationsList.add(n);

        lenient().when(notificationService.notificationAuthorizationAvailableForAnAccount(eq(1)))
                .thenReturn(notificationsList);
        lenient().when(compteService.login(eq("a@gmail.com"),eq("pass"))).thenReturn(a);

        notificationService.verifyNotificationCreated(demandeAuthorisation);

        verify(compteService, times(1)).login(eq("a@gmail.com"),eq("pass"));
    }
    @Test
    void verifyNotificationCreated(){
        Account a = new Account("pass",0.0,1);
        a.setIdAccount(1);
        User user = new User("a@gmail.com","pass");
        DemandeAuthorisation demandeAuthorisation = new DemandeAuthorisation(user,"Demande de synchronisation");

        Notification n = new Notification("Demande de synchronisation",Constante.VALID,1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),"Authorisation");

        Collection<Notification> notificationsList = new ArrayList<>();
        notificationsList.add(n);

        lenient().when(notificationService.notificationAuthorizationAvailableForAnAccount(eq(1)))
                .thenReturn(notificationsList);
        lenient().when(compteService.login(eq("a@gmail.com"),eq("pass"))).thenReturn(a);

        assertThrows(NotificationFoundException.NotificationAuthorisationFoundException.class, () ->  notificationService.verifyNotificationCreated(demandeAuthorisation));
    }
// method newNotification
    @Test
    void createNewNotification() throws BankCustomerNotFoundException, BankAccountNotFoundException {
        Customer c = new Customer("alex","jean","45 rue de tolbiac","a@gmail.com");
        c.setIdCustomer(1);

        Account a = new Account("pass",0.0,1);
        a.setIdAccount(1);
        User user = new User("a@gmail.com","pass");
        DemandeAuthorisation demandeAuthorisation = new DemandeAuthorisation(user,"Demande de synchronisation");

        Notification n = new Notification("Demande de synchronisation",Constante.VALID,1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),"Authorisation");

        lenient().when(customerDAO.findMatchingCustomer(eq("a@gmail.com"))).thenReturn(c);
        lenient().when(accountDAO.findMatchingAccount(eq(1))).thenReturn(a);
        lenient().when(notificationDAO.createNewNotification(eq("Demande de synchronisation"),eq(Constante.VALID),eq(1),
                eq(new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR))),eq("Authorisation")))
                .thenReturn(n);

        notificationService.newNotification(demandeAuthorisation);

        verify(customerDAO,times(1)).findMatchingCustomer("a@gmail.com");
        verify(accountDAO,times(1)).findMatchingAccount(1);
        verify(notificationDAO,times(1)).createNewNotification(any(),eq(Constante.VALID),eq(1),
                eq(new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR))),any());
    }
    @Test
    void accountNotFound() throws BankAccountNotFoundException, BankCustomerNotFoundException {
        User user = new User("a@gmail.com","password");
        DemandeAuthorisation demandeAuthorisation = new DemandeAuthorisation(user,"Demande de synchro");

        lenient().when(customerDAO.findMatchingCustomer("a@gmail.com")).thenReturn(
                new Customer("alex","jean","45 rue de tolbiac","a@gmail.com")
        );

        lenient().doThrow(BankAccountNotFoundException.class)
                .when(accountDAO).findMatchingAccount(eq(0));

        assertThrows(BankAccountNotFoundException.class, () -> notificationService.newNotification(demandeAuthorisation));

        verify(customerDAO,times(1)).findMatchingCustomer("a@gmail.com");
    }
    @Test
    void customerNotFound() throws BankCustomerNotFoundException {
        User user = new User("a@gmail.com","password");
        DemandeAuthorisation demandeAuthorisation = new DemandeAuthorisation(user,"Demande de synchro");


        lenient().doThrow(BankCustomerNotFoundException.class)
                .when(customerDAO).findMatchingCustomer(ArgumentMatchers.matches("a@gmail.com"));


        assertThrows(BankCustomerNotFoundException.class, () -> notificationService.newNotification(demandeAuthorisation));

    }
    //method  updateNotificationHandle
    @Test
    void testUpdateNotificationHandle() {
        Notification beforeUpdateNotification = new Notification("Demande de synchronisation de compte",
                Constante.VALID,
                1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR))
                ,"Authorisation");
        Notification updatedNotification = new Notification("Demande de synchronisation de compte", Constante.EXPIRED, 1,
                new java.sql.Date(cal.get(Calendar.YEAR)-1900,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_YEAR)),
                "Authorisation");

        lenient().when(notificationDAO.updateNotificationEtat(eq(beforeUpdateNotification),eq(Constante.EXPIRED)))
                .thenReturn(updatedNotification);

        Notification n = notificationService.updateNotificationHandle(1);

        verify(notificationDAO, times(1)).findById(1);
        verify(notificationDAO, times(1)).updateNotificationEtat(beforeUpdateNotification,Constante.EXPIRED);

        assertEquals(Constante.EXPIRED, n.getEtat());
    }
}