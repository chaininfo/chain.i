package info.chain.chaini.rate;

/**
 * Created by pocketEos on 2017/11/23.
 * app信息
 */
public class RateUtil {
    private RateUtil() {
        throw new UnsupportedOperationException("AppUtil cannot instantiated");
    }

    public static double getRateEosUsd() {
        return getRate("EOS", "EOS", "USD");
    }
    public static double getRateEosCny() {
        return getRate("EOS", "EOS", "CNY");
    }

    public static double getRateBtcUsd() {
        return getRate("BTC", "BTC", "USD");
    }

    public static double getRateBtcCny() {
        return getRate("BTC", "BTC", "CNY");
    }

    public static double getRateEthUsd() {
        return getRate("ETH", "EOS", "USD");
    }

    public static double getRateEthCny() {
        return getRate("ETH", "EOS", "CNY");
    }

    public static double getRate(String chain, String token, String coin) {
        if(chain.equals("BTC")) {
            if(token.equals("BTC")) {
                if(coin.equals("USD")) {
                    return 6000.0;
                }
                else if(coin.equals("CNY")) {
                    return 40000.0;
                }
            }
        }
        if(chain.equals("ETH")) {
            if(token.equals("EOS")) {
                if(coin.equals("USD")) {
                    return 8.0;
                }
                else if(coin.equals("CNY")) {
                    return 48;
                }
            }
        }

        if(chain.equals("EOS")) {
            if(token.equals("EOS")) {
                if(coin.equals("USD")) {
                    return 8.0;
                }
                else if(coin.equals("CNY")) {
                    return 48;
                }
            }
        }
        return 0;
    }
}
