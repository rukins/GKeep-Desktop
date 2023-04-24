package io.github.rukins.gkeep.objects

import io.github.rukins.gkeepapi.model.gkeep.node.nodeobject.AbstractNode
import io.github.rukins.gkeepapi.model.gkeep.userinfo.Label
import io.github.rukins.gkeepapi.utils.NodeUtils

class UserData(
    var nodes: List<AbstractNode> = mutableListOf(),
    var unsyncNodes: List<AbstractNode> = mutableListOf(),
    var labels: List<Label> = mutableListOf(),
    var unsyncLabels: List<Label> = mutableListOf()
) {
    fun merge(userData: UserData) {
        this.nodes = NodeUtils.mergeListsOfAbstractNodes(this.nodes, userData.nodes)
        this.unsyncNodes = NodeUtils.mergeListsOfAbstractNodes(this.unsyncNodes, userData.unsyncNodes)
        this.labels = NodeUtils.mergeListsOfLabels(this.labels, userData.labels)
        this.unsyncLabels = NodeUtils.mergeListsOfLabels(this.unsyncLabels, userData.unsyncLabels)
    }
}