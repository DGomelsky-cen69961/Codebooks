# used Colman schema name
export DB_APP_SCHEMA=${ONLINE_LOGIN}
if [ -z "$DB_APP_SCHEMA" ]
then 
    export DB_APP_SCHEMA="COLMANDATA_APP"
fi

# used Colman schema password
export DB_APP_PASS=${ONLINE_PASSWORD}
if [ -z "$DB_APP_PASS" ]
then
    echo "No ONLINE password defined! Set it up in CSBIT or manually in config_online/setenv_online.sh"
    exit 1
fi

# sequence stepper for online schema (defines ending digit for all generated PK ids)
export SEQ_STEPPER=1

# enables/disables sending of account reservations (restraints)
export ACC_RESERVATION_ENABLED=true

# enables/disables sending of notifications
export NOTIF_ENABLED=true

# default limit used for processing
export DEFAULT_SKIP_LIMIT=100

# define log directory
export COLMAN_LOG_ROOT="/srv/log/$ENV/colman/batch/batch_processing_online"

