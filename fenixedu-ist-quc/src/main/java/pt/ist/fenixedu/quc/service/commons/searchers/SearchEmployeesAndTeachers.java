/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST QUC.
 *
 * FenixEdu IST QUC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST QUC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST QUC.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.quc.service.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class SearchEmployeesAndTeachers implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Person> result = new ArrayList<Person>();

        if (value == null) {
            result = new ArrayList<Person>(Person.findPerson(""));
        } else {
            for (Person person : Person.findPerson(StringNormalizer.normalize(value))) {
                if ((person.getTeacher() != null && person.getTeacher().isActiveContractedTeacher())
                        || (person.getEmployee() != null && person.getEmployee().isActive())) {
                    result.add(person);
                }
            }
        }

        Collections.sort(result, Comparator.comparing(Person::getName));
        return result;
    }
}
