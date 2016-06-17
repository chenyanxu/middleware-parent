OSGi Template
==========================================

Template for a maven based OSGi project. Simply copy this directory as a starting point for your own bundle

访问couchdb
http://127.0.0.1:5984/carinfo/_design/brand/_view/video?key=["红旗","video"]

view代码：
function(doc) {
if(doc.schema=='brand')
{emit([doc.name,doc.type], doc);}
}

http://localhost:5984/mydb/626b345059c2a54fbe8b8009ba87a409?revs_info=true

//change
http://10.133.77.42:5984/books/_changes?filter=_view&view=filters/import


备注：
需要修改lightcouch的meta文件，修改commons-logging版本为1.1-2
