package br.com.horseconnect.common.manager

import br.com.horseconnect.impl.domain.model.Farm
import br.com.horseconnect.impl.domain.model.User

object SessionManager {
    var user: User? = null
    var farm: Farm? = null
}