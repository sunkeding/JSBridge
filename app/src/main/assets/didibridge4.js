(function (b) {
    if(b.DidiJSBridge) {
        console.log("DidiJSBridge already exist!");
        return;
    }
    console.log("DidiJSBridge initialization begin");
    var DidiJSBridge = {
        queue: [],
        callback: function () {
            var argsArray = Array.prototype.slice.call(arguments, 0);
            var callbackId = argsArray.shift();
            var e = argsArray.shift();
            this.queue[callbackId].apply(this, argsArray);
            if (e) {
                delete this.queue[callbackId]
            }
        },
        init : function(){},
        callHandler: function() {
            var argsArray = Array.prototype.slice.call(arguments, 0);
            if (argsArray.length < 1) {
                throw "DidiJSBridge call error, message:miss handler name"
            }
            var argTypes = [];
            for(var i = 0; i < argsArray.length; i++){
                var arg = argsArray[i];
                var argType = typeof arg;
                argTypes[argTypes.length] = argType;
                if(argType == "function"){
                    var callbackId = DidiJSBridge.queue.length;
                    DidiJSBridge.queue[callbackId] = arg;
                    argsArray[i] = callbackId;
                }
            }
            var g = JSON.parse(prompt(JSON.stringify({
                    method: "callHandler",
                    types: argTypes,
                    args: argsArray,
                    origin: window.location.hostname
                  })));
            if (g.code != 200) {
                throw "DidiJSBridge call error, code:" + g.code + ", message:" + g.result
            }
            return g.result;
        },
        registerHandler : function (name, func){
            window[name] = func
        }
    };

    b.DidiJSBridge = DidiJSBridge;
    var ev = document.createEvent('HTMLEvents');
    ev.initEvent('DidiJSBridgeReady', false, false);
    document.dispatchEvent(ev);
    console.log("DidiJSBridge initialization end")
})(window);