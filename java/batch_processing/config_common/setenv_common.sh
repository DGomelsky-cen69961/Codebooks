# Colman database connection string
export DB_CONNECTION_STRING=$CONNECTION_STRING
if [ -z "$DB_CONNECTION_STRING" ] ; then 
    export DB_CONNECTION_STRING="jdbc:oracle:thin:@//xordb-vip-a.vs.csin.cz:1521/CLMTx"
    echo "DB_CONNECTION_STRING set to default $DB_CONNECTION_STRING, change it to something reasonable."
fi

# used import directory
if [ -z "$CSOPS_IMPORT_DIR" ] ; then 
    export CSOPS_IMPORT_DIR=/srv/tmp/$ENV/colman/batch/batch_processing/import
fi

# used export directory
if [ -z "$CSOPS_EXPORT_DIR" ] ; then 
    export CSOPS_EXPORT_DIR=/srv/tmp/$ENV/colman/batch/batch_processing/export
fi

# My CSOPS node - universal SOURCE NODE for all
if [ -z "$CSOPS_ME" ] ; then
    CSOPS_ME="NOTSET"
    echo "CSOPS_ME not set !!"
fi

# DWH export - CSOPS target node name
if [ -z "$DWH_TARGET_NODE_NAME" ] ; then
    DWH_TARGET_NODE_NAME="NOTSET"
    echo "DWH_TARGET_NODE_NAME not set !!"
fi

# DPOST export - CSOPS target node name
if [ -z "$DPOST_TARGET_NODE_NAME" ] ; then
    DPOST_TARGET_NODE_NAME="NOTSET"
    echo "DPOST_TARGET_NODE_NAME not set !!"
fi

# RISK export - CSOPS target node name
if [ -z "$RISK_TARGET_NODE_NAME" ] ; then
    RISK_TARGET_NODE_NAME="NOTSET"
    echo "RISK_TARGET_NODE_NAME not set !!"
fi

# SAP export - CSOPS target node name
if [ -z "$SAP_TARGET_NODE_NAME" ] ; then
    SAP_TARGET_NODE_NAME="NOTSET"
    echo "SAP_TARGET_NODE_NAME not set !!"
fi

# CMT notifications export - CSOPS target node name
if [ -z "$CMT_NOTIF_TARGET_NODE_NAME" ] ; then
    CMT_NOTIF_TARGET_NODE_NAME="NOTSET"
    echo "CMT_NOTIF_TARGET_NODE_NAME not set !!"
fi


# RMD export - CSOPS target node name
if [ -z "$RMD_TARGET_NODE_NAME" ] ; then
    RMD_TARGET_NODE_NAME="NOTSET"
    echo "RMD_TARGET_NODE_NAME not set !!"
fi


# SmartCase and ELNNV fees export - CSOPS target node name
if [ -z "$SMCASE_TARGET_NODE_NAME" ] ; then
    SMCASE_TARGET_NODE_NAME="NOTSET"
    echo "SMCASE_TARGET_NODE_NAME not set !!"
fi

# BRASIL and ELNNV fees export - CSOPS target node name
if [ -z "$BRASIL_TARGET_NODE_NAME" ] ; then
    BRASIL_TARGET_NODE_NAME="NOTSET"
    echo "BRASIL_TARGET_NODE_NAME not set !!"
fi


# BOS export - CSOPS target node name
if [ -z "$BOS_TARGET_NODE_NAME" ] ; then
    BOS_TARGET_NODE_NAME="NOTSET"
    echo "BOS_TARGET_NODE_NAME not set !!"
fi

# KNProxy export - CSOPS target node name
if [ -z "$KNPROXY_TARGET_NODE_NAME" ] ; then
    KNPROXY_TARGET_NODE_NAME="NOTSET"
    echo "KNPROXY_TARGET_NODE_NAME not set !!"
fi

# CRM export - CSOPS target node name
if [ -z "$CRM_TARGET_NODE_NAME" ] ; then
    CRM_TARGET_NODE_NAME="NOTSET"
    echo "CRM_TARGET_NODE_NAME not set !!"
fi
