/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST Integration.
 *
 * FenixEdu IST Integration is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST Integration is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST Integration.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.integration.ui.spring.controller;

import org.fenixedu.academic.domain.DomainOperationLog;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixedu.integration.domain.BpiCard;
import pt.ist.fenixedu.integration.domain.CardOperationLog;
import pt.ist.fenixedu.integration.domain.SantanderCard;
import pt.ist.fenixedu.integration.domain.cgd.CgdCard;
import pt.ist.fenixedu.integration.ui.spring.service.SendCgdCardService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@SpringApplication(group = "logged", path = "authorize-personal-data-access", title = "authorize.personal.data.access.title")
@SpringFunctionality(app = AuthorizePersonalDataAccessController.class, title = "authorize.personal.data.access.title")
@Controller
@RequestMapping("/authorize-personal-data-access")
public class AuthorizePersonalDataAccessController {

    public SendCgdCardService sendCgdCardService;

    @Autowired
    public AuthorizePersonalDataAccessController(SendCgdCardService sendCgdCardService) {
        this.sendCgdCardService = sendCgdCardService;
    }

    private void addAuthorizationDetailsInfo(Model model, String titleKey, String messageKey) {
        String bundle = "resources.FenixeduIstIntegrationResources";

        String title = BundleUtil.getString(bundle, titleKey);
        String message = BundleUtil.getString(bundle, messageKey);

        model.addAttribute("title", title);
        model.addAttribute("message", message);
    }

    private String checkAuthorizationDetails(Model model, String titleKey, String messageKey) {
        addAuthorizationDetailsInfo(model, titleKey, messageKey);

        return "fenixedu-ist-integration/personalDataAccess/checkAuthorizationDetails";
    }

    private String chooseAuthorizationDetails(Model model, String titleKey, String messageKey) {
        addAuthorizationDetailsInfo(model, titleKey, messageKey);

        return "fenixedu-ist-integration/personalDataAccess/chooseAuthorizationDetails";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String santanderCardAuthorization(HttpServletRequest request, Model model) {
        String santanderCardTitle = "authorize.personal.data.access.title.santander";
        String santanderCardMessage = "authorize.personal.data.access.description.santander.card";

        return checkAuthorizationDetails(model, santanderCardTitle, santanderCardMessage);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String santanderCardAuthorizationSubmit(RedirectAttributes redirectAttributes) {
        SantanderCard.setGrantCardAccess(true, Authenticate.getUser());

        return "redirect:/authorize-personal-data-access/step2";
    }

    @RequestMapping(value = "/step2", method = RequestMethod.GET)
    public String cgdCardAuthorization(Model model) {
        String cgdCardTitle = "authorize.personal.data.access.title.cgd";
        String cgdCardMessage = "authorize.personal.data.access.description.cgd";

        return checkAuthorizationDetails(model, cgdCardTitle, cgdCardMessage);
    }

    @RequestMapping(value = "/step2", method = RequestMethod.POST)
    public String cgdCardAuthorizationSubmit() {
        final CgdCard card = CgdCard.setGrantCardAccess(true);
        if (card != null) {
            sendCgdCardService.asyncSendCgdCard(card);
        }

        return "redirect:/authorize-personal-data-access/step3";
    }

    @RequestMapping(value = "/step3", method = RequestMethod.GET)
    public String bpiCardAuthorization(Model model) {
        String bpiCardTitle = "authorize.personal.data.access.title.bpi";
        String bpiCardMessage = "authorize.personal.data.access.description.bpi";

        return chooseAuthorizationDetails(model, bpiCardTitle, bpiCardMessage);
    }

    @RequestMapping(value = "/step3", method = RequestMethod.POST)
    public String bpiCardAuthorizationSubmit(@RequestParam boolean allowAccess) {
        BpiCard.setGrantCardAccess(allowAccess, Authenticate.getUser());

        return "redirect:/authorize-personal-data-access/step4";
    }

    @RequestMapping(value = "/step4", method = RequestMethod.GET)
    public String santanderBankAuthorization(Model model) {
        String santanderBankTitle = "authorize.personal.data.access.title.santander";
        String santanderBankMessage = "authorize.personal.data.access.description.santander.bank";

        return chooseAuthorizationDetails(model, santanderBankTitle, santanderBankMessage);
    }

    @RequestMapping(value = "/step4", method = RequestMethod.POST)
    public String santanderBankAuthorizationSubmit(@RequestParam boolean allowAccess) {
        SantanderCard.setGrantBankAccess(allowAccess, Authenticate.getUser());

        return "redirect:/authorize-personal-data-access/step5";
    }

    @RequestMapping(value = "/step5", method = RequestMethod.GET)
    public String cgdBankAuthorization(Model model) {
        String cgdCardTitle = "authorize.personal.data.access.title.cgd";
        String cgdCardMessage = "authorize.personal.data.access.description.cgd";

        return chooseAuthorizationDetails(model, cgdCardTitle, cgdCardMessage);
    }

    @RequestMapping(value = "/step5", method = RequestMethod.POST)
    public String cgdBankAuthorizationSubmit(@RequestParam boolean allowAccess) {
        CgdCard.setGrantBankAccess(allowAccess);

        return "redirect:/authorize-personal-data-access/step6";
    }

    @RequestMapping(value = "/step6", method = RequestMethod.GET)
    public String bpiBankAuthorization(Model model) {
        String bpiBankTitle = "authorize.personal.data.access.title.bpi";
        String bpiBankMessage = "authorize.personal.data.access.description.bpi";

        return chooseAuthorizationDetails(model, bpiBankTitle, bpiBankMessage);
    }

    @RequestMapping(value = "/step6", method = RequestMethod.POST)
    public String bpiBankAuthorizationSubmit(@RequestParam boolean allowAccess) {
        BpiCard.setGrantBankAccess(allowAccess, Authenticate.getUser());

        return "redirect:/authorize-personal-data-access/concluded";
    }

    @RequestMapping(value = "/concluded", method = RequestMethod.GET)
    public String concluded() {

        return "fenixedu-ist-integration/personalDataAccess/concludedAuthorizationDetails";
    }

    @RequestMapping(value = "/historic", method = RequestMethod.GET)
    public String authorizationHistoric(Model model) {
        List<DomainOperationLog> cardOperationLogs = Authenticate.getUser().getPerson()
                .getDomainOperationLogsSet().stream().filter(CardOperationLog.class::isInstance)
                .sorted(CardOperationLog.COMPARATOR_BY_WHEN_DATETIME)
                .collect(Collectors.toList());
        //cardOperationLogs.iterator().next().getWhenDateTime().toString
        model.addAttribute("cardAuthorizationLogs", cardOperationLogs);

        return "fenixedu-ist-integration/personalDataAccess/authorizationDetailsHistoric";
    }

}
