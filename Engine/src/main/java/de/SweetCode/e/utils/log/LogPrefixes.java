package de.SweetCode.e.utils.log;

public enum LogPrefixes implements LogPrefix {

    ENGINE {
        @Override
        public String prefix() {
            return "Engine";
        }
    },
    HOT_SWAP {
        @Override
        public String prefix() {
            return "HotSwap";
        }
    },
    DIALOGUE {
        @Override
        public String prefix() {
            return "Dialogue";
        }
    },
    TEXTURE {
        @Override
        public String prefix() {
            return "Texture";
        }
    };

    public static LogPrefix custom(String prefix) {
        return () -> prefix;
    }

}

