package de.tum.in.testuser.subject.structural.solution;

import java.util.Date;
import java.util.List;

public interface SortStrategy {

	/**
	 * Sorts a list of Dates.
	 *
	 * @param input list of Dates
	 */
	void performSort(List<Date> input);
}
