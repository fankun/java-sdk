package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Hyperwallet {

    private final HyperwalletUtil util;
    private final String version = "0.0.2";
    private final String url;

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username  API key assigned
     * @param password  API Password assigned
     * @param url API base url
     * */
    public Hyperwallet(final String username, final String password, final String programToken, final String url) {
        util = new HyperwalletUtil(username, password, programToken, version);
        this.url = StringUtils.isEmpty(url) ? "https://api.sandbox.hyperwallet.com/rest/v3" : url;
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username API key assigned
     * @param password API password
     * @param programToken API program token assigned
     * */
    public Hyperwallet(final String username, final String password, final String programToken) {
        this(username, password, programToken, null);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username API key assigned
     * @param password API password
     * */
    public Hyperwallet(final String username, final String password) {
        this(username, password, null);
    }

    /**
     * Create a User
     *
     * @param user Hyperwallet user representation
     * @return HyperwalletUser created User
     * @throws HyperwalletException
     * */
    public HyperwalletUser createUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (!StringUtils.isEmpty(user.getToken())) {
            throw new HyperwalletException("User token may not be present");
        }
        user = util.clean(user);
        user.setStatus(null);
        user.setCreatedOn(null);
        return util.post(url + "/users", user, HyperwalletUser.class);
    }

    /**
     * Get User
     *
     * @param token user account token
     * @return HyperwalletUser retreived user
     * @throws HyperwalletException
     * */
    public HyperwalletUser getUser(String token) {
        return util.get(url + "/users/" + token, HyperwalletUser.class);
    }

    /**
     * Update User
     *
     * @param user Hyperwallet User representation object
     * @return HyperwalletUser updated user object
     * @throws HyperwalletException
     * */
    public HyperwalletUser updateUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (StringUtils.isEmpty(user.getToken())) {
            throw new HyperwalletException("User token is required");
        }
        return util.put(url + "/users/" + user.getToken(), user, HyperwalletUser.class);
    }

    /**
     * List Users
     * @return HyperwalletList<HyperwalletUser> list of Users
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletUser> listUsers() {
        return listUsers(null);
    }

    /**
     * List Users
     * @param options List filter option
     * @return HyperwalletList<HyperwalletUser> list of Users
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletUser> listUsers(HyperwalletPaginationOptions options) {
        String url = paginate(this.url + "/users", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletUser>>() {});
    }

    /**
     * Create Prepaid Card
     *
     * @param prepaidCard Prepaid Card object to create
     * @return HyperwalletPrepaidCard Prepaid Card object created
     * @throws HyperwalletException
     * */
    public HyperwalletPrepaidCard createPrepaidCard(HyperwalletPrepaidCard prepaidCard) {
        if (prepaidCard == null) {
            throw new HyperwalletException("Card is required");
        }
        if (StringUtils.isEmpty(prepaidCard.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(prepaidCard.getToken())) {
            throw new HyperwalletException("Prepaid Card token may not be present");
        }
        prepaidCard = util.clean(prepaidCard);
        prepaidCard.createdOn(null);
        prepaidCard.setStatus(null);
        prepaidCard.setCardType(null);
        prepaidCard.setCreatedOn(null);
        prepaidCard.setTransferMethodCountry(null);
        prepaidCard.setTransferMethodCurrency(null);
        prepaidCard.setCardNumber(null);
        prepaidCard.setCardBrand(null);
        prepaidCard.setDateOfExpiry(null);
        return util.post(url + "/users/" + prepaidCard.getUserToken() + "/prepaid-cards", prepaidCard, HyperwalletPrepaidCard.class);
    }

    /**
     * Get Prepaid Card
     *
     * @param userToken User token assigned
     * @param prepaidCardToken Prepaid Card token
     * @return HyperwalletPrepaidCard Prepaid Card
     * @throws HyperwalletException
     * */
    public HyperwalletPrepaidCard getPrepaidCard(String userToken, String prepaidCardToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Card token is required");
        }
        return util.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken, HyperwalletPrepaidCard.class);
    }

    /**
     * List User's Prepaid Card
     *
     * @param userToken User token assigned
     * @return HyperwalletList<HyperwalletPrepaidCard> list of Prepaid cards
     * */
    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken) {
        return listPrepaidCards(userToken, null);
    }

    /**
     * List User's Prepaid Card
     *
     * @param userToken User token assigned
     * @param options List filter option
     * @return HyperwalletList<HyperwalletPrepaidCard> list of Prepaid cards
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletPrepaidCard>>() {
        });
    }

    /**
     * Create User Bank Account
     *
     * @param bankAccount bank account representation
     * @return HyperwalletBankAccount created bank account for the specicic user
     * @throws HyperwalletException
     * */
    public HyperwalletBankAccount createUserBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Transfer Method is required");
        }
        if (StringUtils.isEmpty(bankAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(bankAccount.getToken())) {
            throw new HyperwalletException("Transfer Method token may not be present");
        }
        bankAccount = util.clean(bankAccount);
        bankAccount.createdOn(null);
        bankAccount.setStatus(null);
        bankAccount.setBranchAddressLine2(null);
        bankAccount.setIntermediaryBankAddressLine2(null);
        bankAccount.setIntermediaryBankStateProvince(null);
        bankAccount.setIntermediaryBankPostalCode(null);
        return util.post(url + "/users/" + bankAccount.getUserToken() + "/bank-accounts", bankAccount, HyperwalletBankAccount.class);
    }

    /**
     * Get User Bank Account
     *
     * @param userToken User token assigned
     * @param transferMethodToken Bank account token assigned
     * @return HyperwalletBankAccount bank account information
     * @throws HyperwalletException
     * */
    public HyperwalletBankAccount getUserBankAccount(String userToken, String transferMethodToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(transferMethodToken)) {
            throw new HyperwalletException("Bank account token is required");
        }
        return util.get(url + "/users/" + userToken + "/bank-accounts/" + transferMethodToken, HyperwalletBankAccount.class);
    }

    /**
     * Update User Bank Account
     *
     * @param bankAccount Bank Account to update.
     * @return HyperwalletBankAccount updated Bank Account
     * @throws HyperwalletException
     * */
    public HyperwalletBankAccount updateUserBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Bank account is required");
        }
        if (StringUtils.isEmpty(bankAccount.getToken())) {
            throw new HyperwalletException("Bank account token is required");
        }
        if (StringUtils.isEmpty(bankAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        return util.put(url + "/users/" + bankAccount.getUserToken() + "/bank-accounts/" + bankAccount.getToken(), bankAccount, HyperwalletBankAccount.class);
    }

    /**
     * List User Bank Accounts
     *
     * @param userToken User token assigned
     * @return HyperwalletList<HyperwalletBankAccount> list of Bank Accounts for the specified User
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(String userToken) {
        return listUserBankAccounts(userToken, null);
    }

    /**
     * List User Bank Accounts
     *
     * @param userToken User token assigned
     * @param options List filter option
     * @return HyperwalletList<HyperwalletBankAccount> list of Bank Accounts for the specified User
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(String userToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletBankAccount>>() {
        });
    }

    /**
     * Create Prepaid Card Status Transition
     *
     * @param userToken Usert token
     * @param prepaidCardToken Prepaid Card token
     * @param transition Status transition information
     * @return HyperwalletStatusTransition new status for Prepaid Card
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition createPrepaidCardStatusTransition(String userToken, String prepaidCardToken, HyperwalletStatusTransition transition) {
        if (prepaidCardToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        transition = util.clean(transition);
        transition.setToken(null);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return util.post(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }


    /**
     * Get Prepaid Card Status Transition
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid Card token
     * @param statusTransitionToken Status transition token
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition getPrepaidCardStatusTransition(String userToken, String prepaidCardToken, String statusTransitionToken) {
        if (prepaidCardToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (statusTransitionToken == null) {
            throw new HyperwalletException("Transition token is required");
        }
        return util.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions" + statusTransitionToken, HyperwalletStatusTransition.class);
    }

    /**
     * List All Prepaid Card Status Transition information
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid Card token
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken) {
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Card token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        return listPrepaidCardStatusTransitions(userToken, prepaidCardToken, null);
    }

    /**
     * List Prepaid Card Status Transition information
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid Card token
     * @param options List filter option
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Card token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {});
    }


    /**
     * Deactivate Bank Account
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @return HyperwalletStatusTransition deactivated bank account
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition deactivateBankAccount(String userToken, String bankAccountToken) {
        return createBankAccountStatusTransition(userToken, bankAccountToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Activate Bank Account
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @return HyperwalletStatusTransition activated bank account
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition activateBankAccount(String userToken, String bankAccountToken) {
        return createBankAccountStatusTransition(userToken, bankAccountToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.ACTIVATED));
    }

    /**
     * Create Bank Account Status Transition
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @param transition Status transition information
     * @return HyperwalletStatusTransition
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition createBankAccountStatusTransition(String userToken, String bankAccountToken, HyperwalletStatusTransition transition) {
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Transition token may not be present");
        }
        transition = util.clean(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return util.post(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    /**
     * Get Bank Account Status Transition
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @param statusTransitionToken Status transition token
     * @throws HyperwalletException
     * */
    public HyperwalletStatusTransition getBankAccountStatusTransition(String userToken, String bankAccountToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return util.get(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions" + statusTransitionToken, HyperwalletStatusTransition.class);
    }


    /**
     * List All Bank Account Status Transition
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken) {
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        return listBankAccountStatusTransitions(userToken, bankAccountToken, null);
    }


    /**
     * List Bank Account Status Transition
     *
     * @param userToken User token
     * @param bankAccountToken Bank Account token
     * @param options List filter option
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {});
    }

    /**
     * Create Payment
     *
     * @param payment Payment
     * @return HyperwalletPayment created payment information
     * @throws HyperwalletException
     * */
    public HyperwalletPayment createPayment(HyperwalletPayment payment) {
        if (payment == null) {
            throw new HyperwalletException("Payment is required");
        }
        if (!StringUtils.isEmpty(payment.getToken())) {
            throw new HyperwalletException("Payment token may not be present");
        }
        payment = util.clean(payment);
        payment.setCreatedOn(null);
        return util.post(url + "/payments/", payment, HyperwalletPayment.class);
    }

    /**
     * Get Payment
     *
     * @param paymentToken Payment token
     * @throws HyperwalletException
     * */
    public HyperwalletPayment getPayment(String paymentToken) {
        return util.get(url + "/payments/" + paymentToken, HyperwalletPayment.class);
    }

    /**
     * List all Payments
     *
     * @return HyperwalletList<HyperwalletPayment> list of Payments
     * */
    public HyperwalletList<HyperwalletPayment> listPayments() {
        return listPayments(null);
    }

    /**
     * List all Payments
     *
     * @param options List filter option
     * @return HyperwalletList<HyperwalletPayment> list of Payments
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletPayment> listPayments(HyperwalletPaymentListOptions options) {
        String url = paginate(this.url + "/payments", options);
        if (options != null) {
            url = addParameter(url, "releasedOn", convert(options.releasedOn));
            url = addParameter(url, "currency", options.currency);
        }
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletPayment>>() {
        });
    }

    /**
     * Get Transfer Method Configuration
     *
     * @param userToken User token
     * @param country Country
     * @param currency Currency
     * @param type Type of Transfer Method to retrieve
     * @param profileType Type of User profile
     * @return HyperwalletTransferMethodConfiguration
     * @throws HyperwalletException
     * */
    public HyperwalletTransferMethodConfiguration getTransferMethodConfiguration(String userToken, String country, String currency, HyperwalletTransferMethod.Type type, HyperwalletUser.ProfileType profileType) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(country)) {
            throw new HyperwalletException("Country is required");
        }
        if (StringUtils.isEmpty(currency)) {
            throw new HyperwalletException("Currency is required");
        }
        if (type == null) {
            throw new HyperwalletException("Type is required");
        }
        if (profileType == null) {
            throw new HyperwalletException("Profile type is required");
        }
        return util.get(url + "/transfer-method-configurations"
                        + "?userToken=" + userToken
                        + "&country=" + country
                        + "&currency=" + currency
                        + "type=" + type.name()
                        + "profileType=" + profileType.name(),
                HyperwalletTransferMethodConfiguration.class);
    }

    /**
     * List all Transfer Method Configuration associated with User
     *
     * @param userToken User token
     * @return HyperwalletList<HyperwalletTransferMethodConfiguration> list of Transfer Method Configuration
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken) {
        return listTransferMethodConfigurations(userToken, null);
    }

    /**
     * List all Transfer Method Configuration associated with User
     *
     * @param userToken User token
     * @param options List filter options
     * @return HyperwalletList<HyperwalletTransferMethodConfiguration> list of Transfer Method Configuration
     * @throws HyperwalletException
     * */
    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken, HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/transfer-method-configurations?userToken=" + userToken, options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethodConfiguration>>() {
        });
    }

    /**
     * List all User's Balances
     *
     * @param userToken User token assigned
     * @throws HyperwalletException
     */
    public HyperwalletList<HyperwalletBalance> listUserBalances(String userToken) {
        return listUserBalances(userToken, null);
    }

    /**
     * List all User's Balances
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @throws HyperwalletException
     */
    public HyperwalletList<HyperwalletBalance> listUserBalances(String userToken, HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = this.url + "/users/" + userToken + "/balances";
        if (options != null) {
            url = addParameter(url, "currency", options.getCurrency());
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "offset", options.getOffset());
            url = addParameter(url, "limit", options.getLimit());
        }
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }

    /**
     * List all User's Prepaid Card Balances
     *
     * @param userToken        User token assigned
     * @param prepaidCardToken Prepaid Card token assigned from User's Prepaid Card
     * @throws HyperwalletException
     */
    public HyperwalletList<HyperwalletBalance> listUserPrepaidCardBalances(String userToken, String prepaidCardToken) {
        return listUserPrepaidCardBalances(userToken, prepaidCardToken, null);
    }

    /**
     * List all User's Prepaid Card Balances
     *
     * @param userToken        User token assigned
     * @param prepaidCardToken Prepaid Card token assigned from User's Prepaid Card
     * @param options          List filter option
     */
    public HyperwalletList<HyperwalletBalance> listUserPrepaidCardBalances(String userToken, String prepaidCardToken, HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid card token is required");
        }
        String url = this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/balances";
        if (options != null) {
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "offset", options.getOffset());
            url = addParameter(url, "limit", options.getLimit());
        }
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }


    /**
     * List Programs Account Balances
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @throws HyperwalletException
     */
    public HyperwalletList<HyperwalletBalance> listProgramAccountBalances(String programToken, String accountToken) {
        return listProgramAccountBalances(programToken, accountToken, null);
    }

    /**
     * List Programs Account Balances
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @param options      List filter option
     * @throws HyperwalletException
     */
    public HyperwalletList<HyperwalletBalance> listProgramAccountBalances(String programToken, String accountToken, HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(programToken)) {
            throw new HyperwalletException("Program token is required");
        }
        if (StringUtils.isEmpty(accountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        String url = this.url + "/programs/" + programToken + "/accounts/" + accountToken + "/balances";
        if (options != null) {
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "offset", options.getOffset());
            url = addParameter(url, "limit", options.getLimit());
        }
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }

    /**
     * Get Programs Account
     *
     * @param programToken Program token
     * @param accountToken Program account token
     */
    public HyperwalletAccount getProgramAccount(final String programToken, final String accountToken){

        if(StringUtils.isEmpty(programToken) ){
            throw new HyperwalletException("Program token is required");
        }

        if(StringUtils.isEmpty(accountToken)){
            throw new HyperwalletException("Account token is required");
        }

        return util.get(url + "/programs/" + programToken + "/accounts/"+ accountToken,  HyperwalletAccount.class);
    }


    /**
     * Get Program
     *
     * @param token Program token
     * @throws HyperwalletException
     */
    public HyperwalletProgram getProgram(String token) {
        if (token == null || token.trim().equals("")) {
            throw new HyperwalletException("Program token is required");
        }
        return util.get(url + "/programs/" + token, HyperwalletProgram.class);
    }

    String paginate(String url, HyperwalletPaginationOptions options) {
        if (options == null) {
            return url;
        }
        url = addParameter(url, "createdAfter", convert(options.createdAfter));
        url = addParameter(url, "createdBefore", convert(options.createdBefore));
        url = addParameter(url, "sortBy", options.sortBy);
        url = addParameter(url, "offset", options.offset);
        url = addParameter(url, "limit", options.limit);
        return url;
    }

    String addParameter(String url, String key, Object value) {
        if (url == null || key == null || value == null) {
            return url;
        }
        return url + (url.indexOf("?") == -1 ? "?" : "&") + key + "=" + value;
    }

    String convert(Date in) {
        if (in == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(in);
    }
}
