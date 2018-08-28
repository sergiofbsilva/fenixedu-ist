package pt.ist.fenixedu.integration.domain;

import java.time.Year;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class SantanderCard extends SantanderCard_Base {

    public SantanderCard(User user) {
        super();
        setUser(user);
    }
    
    public SantanderCard(User user, boolean allow) {
        super();
        setUser(user);
        setAllowSendDetails(allow);
    }

    @Atomic
    public static void setGrantAccess(final boolean allowAccess, final User user) {
        if (user != null) {
            final SantanderCard card = user.getSantanderCard();
            if (card != null) {
                card.setAllowSendDetails(allowAccess);
            } else {
                new SantanderCard(user, allowAccess);
            }
        }
    }

    @Atomic
    public static void setGrantCardAccess(final boolean allowCardAccess, final User user) {
        if (user != null) {
            final SantanderCard card = user.getSantanderCard();
            if (card != null) {
                card.setAllowSendCardDetails(allowCardAccess);
            } else {
                SantanderCard santanderCard = new SantanderCard(user);
                santanderCard.setAllowSendCardDetails(allowCardAccess);
            }
            CardOperationLog cardOperationLog = new CardOperationLog();
            cardOperationLog.setDescription("Santander - Tomei conhecimento cartão");
        }
    }

    @Atomic
    public static void setGrantBankAccess(final boolean allowBankAccess, final User user) {
        if (user != null) {
            final SantanderCard card = user.getSantanderCard();
            if (card != null) {
                card.setAllowSendBankDetails(allowBankAccess);
            } else {
                SantanderCard santanderCard = new SantanderCard(user);
                santanderCard.setAllowSendBankDetails(allowBankAccess);
            }
            CardOperationLog cardOperationLog = new CardOperationLog();
            String authorization = allowBankAccess ? "Autorizo" : "Não autorizo";
            cardOperationLog.setDescription("Santander - " + authorization + "banco");
        }
    }

    @Override
    public void setAllowSendDetails(boolean allow) {
        super.setAllowSendDetails(allow);
        setWhenAllowChanged(new DateTime());
    }

    @Override
    public void setAllowSendCardDetails(boolean allow) {
        super.setAllowSendCardDetails(allow);
        setWhenCardAllowChanged(new DateTime());
    }

    @Override
    public void setAllowSendBankDetails(boolean allow) {
        super.setAllowSendBankDetails(allow);
        setWhenBankAllowChanged(new DateTime());
    }

    public static boolean hasAccessResponse() {
        final User user = Authenticate.getUser();
        final int year = Year.now().getValue();
        return user.getSantanderCard() != null && user.getSantanderCard().getWhenAllowChanged().getYear() == year;
    }
    
}
