# used Colman schema name
export DB_APP_SCHEMA=${OFFLINE_LOGIN}
if [ -z "$DB_APP_SCHEMA" ] ; then 
    export DB_APP_SCHEMA="COLMANWORK_APP"
fi

# used Colman schema password
export DB_APP_PASS=${OFFLINE_PASSWORD}
if [ -z "$DB_APP_PASS" ] ; then 
    echo "No OFFLINE password defined! Set it up in CSBIT or manually in config_offline/setenv_offline.sh"
    exit 1
fi

# sequence stepper for online schema (defines ending digit for all generated PK ids)
export SEQ_STEPPER=3

# enables/disables sending of account reservations (restraints)
export ACC_RESERVATION_ENABLED=false

# enables/disables sending of notifications
export NOTIF_ENABLED=false

# default limit used for processing
export DEFAULT_SKIP_LIMIT=100

# define log directory
export COLMAN_LOG_ROOT="/srv/log/$ENV/colman/batch/batch_processing_offline"
