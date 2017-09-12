package pt.ist.registration.process.domain;

import java.util.Locale;
import java.util.Optional;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.User;

public class RegistrationDeclarationFile extends RegistrationDeclarationFile_Base {

    public RegistrationDeclarationFile(String filename, byte[] content, DeclarationTemplate declarationTemplate, Registration
            registration, ExecutionYear executionYear,  Locale locale, String uniqueIdentifier) {
        super();
        getRegistrationDeclarationFile(registration, executionYear, declarationTemplate, locale).ifPresent
                (RegistrationDeclarationFile::delete);
        init(filename, filename, content);
        setUniqueIdentifier(uniqueIdentifier);
        setRegistration(registration);
        setExecutionYear(executionYear);
        setLocale(locale);
        setDeclarationTemplate(declarationTemplate);
    }

    @Override
    public void delete() {
        setDeclarationTemplate(null);
        setRegistration(null);
        setExecutionYear(null);
        super.delete();
    }

    @Override
    public boolean isAccessible(User user) {
        return false;
    }

    public static Optional<RegistrationDeclarationFile> getRegistrationDeclarationFile(Registration registration, ExecutionYear
            executionYear, DeclarationTemplate declarationTemplate, Locale locale) {
        return registration.getRegistrationDeclarationFileSet().stream().filter(file -> file.getExecutionYear() ==
                executionYear && file.getDeclarationTemplate() == declarationTemplate && file.getLocale() == locale ).findAny();
    }

    public static Optional<RegistrationDeclarationFile> getRegistrationDeclarationFile(Registration registration, String
    uniqueIdentifier) {
        return registration.getRegistrationDeclarationFileSet().stream().filter(file -> file.getUniqueIdentifier().equals
        (uniqueIdentifier)).findAny();
    }

}
