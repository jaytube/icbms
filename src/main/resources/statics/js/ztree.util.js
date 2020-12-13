var MtrSearchZTree=function(l){var location=l||".ztree";var nodesAll={};var showNodesAll={};var keywords="";var parentNodes=new Map();function filterFunc(node){if(node.name.indexOf(keywords)!=-1){return true;}else{return false;}}
function findParent(ztreeObj,node,showNodes){ztreeObj.expandNode(node,true,false,false);var pNode=node.getParentNode();if(pNode!=null){if(parentNodes.get(pNode.tId)==null){parentNodes.set(pNode.tId,pNode);showNodes.push(pNode);findParent(ztreeObj,pNode,showNodes);}}}
function bindingZTree(id){$("#"+id+"Keyword").on("input change",function(e){parentNodes=new Map();keywords=$(this).val();var ztreeObj=$.fn.zTree.getZTreeObj(id);if(keywords){var showNodes=showNodesAll[id];ztreeObj.hideNodes(ztreeObj.getNodesByParam("isHidden",false));showNodes=ztreeObj.getNodesByFilter(filterFunc);showNodes=ztreeObj.transformToArray(showNodes);for(var n in showNodes){if(showNodes.hasOwnProperty(n)){findParent(ztreeObj,showNodes[n],showNodes);}}
ztreeObj.showNodes(showNodes);showNodesAll[id]=showNodes;}else{ztreeObj.showNodes(ztreeObj.getNodesByParam("isHidden",true));}});}
function setAllNodes(id){var ztreeObj=$.fn.zTree.getZTreeObj(id);if(ztreeObj){var nodes=ztreeObj.getNodes();nodesAll[id]=ztreeObj.transformToArray(nodes);showNodesAll[id]=[];}}
function initSearchZTree(){var ztrees=$(location);for(let i=0;i<ztrees.length;i++){var ztree=ztrees.eq(i);var ztreeId=ztree.attr("id");ztree.children("input").remove();var html='<input id="'+ztreeId+'Keyword" type="text" class="form-control" placeholder="请输入关键字...">';ztree.prepend(html);setAllNodes(ztreeId);bindingZTree(ztreeId);}}
initSearchZTree();return{getshowNodesById:function(id){if(id){return showNodesAll[id];}},getshowNodes(){return showNodesAll;}}}