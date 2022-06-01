package de.tum.in.test.integration.testuser;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.*;
import de.tum.in.test.api.localization.UseLocale;

@UseLocale("en")
@Deadline("2000-01-01 00:00")
public class DeadlineAdditionsUser {

	@HiddenTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	@Deadline("2200-01-01 16:00")
	void testHidden_CustomDeadlineFutureActive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	@Deadline("2200-01-01 16:00")
	@ExtendedDeadline("50000d")
	void testHidden_CustomDeadlineFutureExtendedActive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2000-01-01 00:00")
	@Deadline("2200-01-01 16:00")
	@ExtendedDeadline("50000d")
	void testHidden_CustomDeadlineFutureExtendedInactive() {
		// nothing
	}

	@HiddenTest
	@ExtendedDeadline("50000d")
	@Deadline("2200-01-01 16:00")
	void testHidden_CustomDeadlineFutureExtendedNormal() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2000-01-01 00:00")
	@Deadline("2200-01-01 16:00")
	void testHidden_CustomDeadlineFutureInactive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	@ExtendedDeadline("50000d")
	@Deadline("2000-01-01 16:00")
	void testHidden_CustomDeadlinePastExtendedActive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2000-01-01 00:00")
	@ExtendedDeadline("50000d")
	@Deadline("2000-01-01 16:00")
	void testHidden_CustomDeadlinePastExtendedInactive() {
		// nothing
	}

	@HiddenTest
	@ExtendedDeadline("50000d")
	@Deadline("2000-01-01 16:00")
	void testHidden_CustomDeadlinePastExtendedNormal() {
		// nothing
	}

	@PublicTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	void testHiddenActive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	@ExtendedDeadline("50000d")
	void testHiddenExtendedActive() {
		// nothing
	}

	@HiddenTest
	@ActivateHiddenBefore("2000-01-01 00:00")
	@ExtendedDeadline("50000d")
	void testHiddenExtendedInactive() {
		// nothing
	}

	@HiddenTest
	@ExtendedDeadline("50000d")
	void testHiddenExtendedNormal() {
		// nothing
	}

	@PublicTest
	@ActivateHiddenBefore("2000-01-01 00:00")
	void testHiddenInactive() {
		// nothing
	}

	@PublicTest
	void testHiddenNormal() {
		// nothing
	}

	@PublicTest
	@ActivateHiddenBefore("2200-01-01 00:00")
	void testPublicActive() {
		// nothing
	}

	@PublicTest
	@ExtendedDeadline("50000d")
	void testPublicExtended() {
		// nothing
	}
}
