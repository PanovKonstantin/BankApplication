import java.util.Random;

public class AccountGenerator {

	private int generateCheckDigit(String account) {
		int[] ints = new int[account.length()];
		for (int i = 0; i < account.length(); i++) {
			ints[i] = Integer.parseInt(account.substring(i, i + 1));
		}
		for (int i = ints.length - 2; i >= 0; i = i - 2) {
			int j = ints[i];
			j = j * 2;
			if (j > 9) {
				j = j % 10 + 1;
			}
			ints[i] = j;
		}
		int sum = 0;
		for (int i = 0; i < ints.length; i++) {
			sum += ints[i];
		}
		if (sum % 10 == 0) {
			return 0;
		} else
			return 10 - (sum % 10);
	}

	public String generateAccount() {
		String account = "";
		Random rand = new Random();
		for (int i = 0; i < 25; i++) {
			account += Integer.toString(rand.nextInt(10));
		}
		int cd = generateCheckDigit(account);
		return account + Integer.toString(cd);
	}

}