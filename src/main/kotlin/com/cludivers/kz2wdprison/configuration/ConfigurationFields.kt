package com.cludivers.kz2wdprison.configuration

enum class ConfigurationFields {
    CONNECT_DATABASE {
        override val fieldName: String
            get() = "connectDatabase"
    },
    DATABASE_LOGIN {
        override val fieldName: String
            get() = "databaseLogin"
    },
    DATABASE_PASSWORD {
        override val fieldName: String
            get() = "databasePassword"
    },
    DATABASE_PORT {
        override val fieldName: String
            get() = "databasePort"
    },
    DATABASE_NAME {
        override val fieldName: String
            get() = "databaseName"
    },
    DATABASE_HBM {
        override val fieldName: String
            get() = "databaseHBM"
    },
    IS_IN_PRODUCTION {
        override val fieldName: String
            get() = "inProduction"

    };

    abstract val fieldName: String


}