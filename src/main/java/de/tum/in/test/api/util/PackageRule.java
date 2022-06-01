package de.tum.in.test.api.util;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.*;

@API(status = Status.INTERNAL)
public final class PackageRule {
	private final RuleType ruleType;
	private final String packagePattern;
	private final Pattern regexPattern;

	private PackageRule(RuleType ruleType, String packagePattern) {
		this.ruleType = ruleType;
		this.packagePattern = packagePattern;
		this.regexPattern = convertPackagePatternToRegex(packagePattern);
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public String getPackagePattern() {
		return packagePattern;
	}

	public Pattern getRegexPattern() {
		return regexPattern;
	}

	public boolean matches(String packageName) {
		return regexPattern.matcher(packageName).matches();
	}

	@Override
	public String toString() {
		return String.format("PackageRule[\"%s\" in %s]", packagePattern, ruleType); //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		return Objects.hash(packagePattern, ruleType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PackageRule))
			return false;
		PackageRule other = (PackageRule) obj;
		return Objects.equals(packagePattern, other.packagePattern) && ruleType == other.ruleType;
	}

	private static Pattern convertPackagePatternToRegex(String packagePattern) {
		String[] parts = Pattern.quote(packagePattern).split("\\*\\*", -1); //$NON-NLS-1$
		for (var i = 0; i < parts.length; i++)
			parts[i] = parts[i].replace("*", "\\E[^.]*\\Q"); //$NON-NLS-1$ //$NON-NLS-2$
		return Pattern.compile(String.join("\\E.*\\Q", parts), Pattern.DOTALL); //$NON-NLS-1$
	}

	public static Stream<PackageRule> allOf(WhitelistPackage whitelistedPackage) {
		return Stream.of(whitelistedPackage.value())
				.map(packagePattern -> new PackageRule(RuleType.WHITELIST, packagePattern));
	}

	public static Stream<PackageRule> allOf(BlacklistPackage blacklistedPackage) {
		return Stream.of(blacklistedPackage.value())
				.map(packagePattern -> new PackageRule(RuleType.BLACKLIST, packagePattern));
	}

	public static Stream<PackageRule> from(RuleType ruleType, String... packagePatterns) {
		return Stream.of(packagePatterns).map(packagePattern -> new PackageRule(ruleType, packagePattern));
	}

	public static Stream<PackageRule> from(RuleType ruleType, Collection<String> packagePatterns) {
		return packagePatterns.stream().map(packagePattern -> new PackageRule(ruleType, packagePattern));
	}
}
