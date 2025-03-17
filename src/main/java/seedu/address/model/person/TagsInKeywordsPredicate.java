package seedu.address.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Tag} are any of the keywords given.
 */
public class TagsInKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagsInKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(this.concatenateTags(person.getTags()), keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagsInKeywordsPredicate)) {
            return false;
        }

        TagsInKeywordsPredicate otherNameContainsKeywordsPredicate = (TagsInKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }

    private String concatenateTags(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getTagName)
                .reduce("", (acc, tag) -> acc + tag + " ");
    }
}

