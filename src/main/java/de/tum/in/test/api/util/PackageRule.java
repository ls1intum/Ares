package de.tum.in.test.api.util;

import java.util.Objects;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.BlacklistPackage;
import de.tum.in.test.api.WhitelistPackage;

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
		return String.format("PackageRule[\"%s\" in %s]", packagePattern, ruleType);
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
		String[] parts = Pattern.quote(packagePattern).split("\\*\\*", -1);
		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].replace("*", "\\E[^.]*\\Q");
		}
		return Pattern.compile(String.join("\\E.*\\Q", parts), Pattern.DOTALL);
	}

	public static PackageRule of(WhitelistPackage whitelistedPath) {
		return new PackageRule(RuleType.WHITELIST, whitelistedPath.value());
	}

	public static PackageRule of(BlacklistPackage blacklistedPath) {
		return new PackageRule(RuleType.BLACKLIST, blacklistedPath.value());
	}
}
