package pt.ist.fenixedu.integration.domain;

import java.time.Year;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class BpiCard extends BpiCard_Base {

    public BpiCard(User user) {
        super();
        setUser(user);
    }

    public BpiCard(User user, boolean allow) {
        super();
        setUser(user);
        setAllowSendDetails(allow);
    }

    @Atomic
    public static void setGrantAccess(final boolean allowAccess, final User user) {
        if (user != null) {
            final BpiCard card = user.getBpiCard();
            if (card != null) {
                card.setAllowSendDetails(allowAccess);
            } else {
                new BpiCard(user, allowAccess);
            }
        }
    }

    @Atomic
    public static void setGrantCardAccess(final boolean allowCardAccess, final User user) {
        if (user != null) {
            final BpiCard card = user.getBpiCard();
            if (card != null) {
                card.setAllowSendCardDetails(allowCardAccess);
            } else {
                BpiCard bpiCard = new BpiCard(user, allowCardAccess);
                bpiCard.setAllowSendCardDetails(allowCardAccess);
            }
            CardOperationLog cardOperationLog = new CardOperationLog();
            String authorization = allowCardAccess ? "Autorizo" : "Não autorizo";
            cardOperationLog.setDescription("BPI - " + authorization + " a cedência cartão");
        }
    }

    @Atomic
    public static void setGrantBankAccess(final boolean allowBankAccess, final User user) {
        if (user != null) {
            final BpiCard card = user.getBpiCard();
            if (card != null) {
                card.setAllowSendBankDetails(allowBankAccess);
            } else {
                BpiCard bpiCard = new BpiCard(user, allowBankAccess);
                bpiCard.setAllowSendBankDetails(allowBankAccess);
            }
            CardOperationLog cardOperationLog = new CardOperationLog();
            String authorization = allowBankAccess ? "Autorizo" : "Não autorizo";
            cardOperationLog.setDescription("BPI - " + authorization + " a cedência banco");
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
        return user.getBpiCard() != null && user.getBpiCard().getWhenAllowChanged().getYear() == year;
    }
}
