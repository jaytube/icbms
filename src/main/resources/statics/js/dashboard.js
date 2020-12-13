$(function() {
	var control = {
		init_page: function() {
			control.initVue();
			control.handleClick();
		},
		handleClick: function() {
			$(".c_box").click(function() {
				var left = $(".c_white").css("left");
				if (left == "32px") {
					$(".c_white").animate({
						left: '2px'
					}, 500);
					$(this).css("backgroundColor", "#989898")
				} else {
					$(".c_white").animate({
						left: '32px'
					}, 500)
					$(this).css("backgroundColor", "#1b9f94")
				}
			});
			
		},
		initVue: function() {
			var app = new Vue({
				el: "#app",
				data: {},
				methods: {
					select: function(e) {
						console.log(e)
						console.log(e.currentTarget)
						console.log(e.target)
						if ($(e.currentTarget).css("border") == "2px solid rgb(27, 159, 148)") {
							$(e.currentTarget).css("border", "none")
							console.log("取消选中")
						} else {
							$(e.currentTarget).css("border", "2px solid #1b9f94")
							console.log("选中")
						}
					},
					li_con:function(e){
						event.stopPropagation()
						if($(e.currentTarget).find($(".li_white")).css("left") == "26px"){
							$(e.currentTarget).find($(".li_white")).animate({left:'2px'},500)
							$(e.currentTarget).css("backgroundColor", "#989898")
							$(e.currentTarget).parent().parent().css("backgroundColor","#f9f9f9")
							$(e.currentTarget).parent().parent().find($(".span1")).css("backgroundColor","red")
							$(e.currentTarget).parent().parent().find($(".span2")).css("backgroundColor","red")
							$(e.currentTarget).parent().parent().find($(".span3")).text("已断")
							$(e.currentTarget).parent().parent().find($(".images")).css("backgroundColor","#989898")
						}else{
							$(e.currentTarget).find($(".li_white")).animate({left:'26px'},500)
							$(e.currentTarget).css("backgroundColor", "#1b9f94")
							$(e.currentTarget).parent().parent().css("backgroundColor","#d1ecea")
							$(e.currentTarget).parent().parent().find($(".span1")).css("backgroundColor","green")
							$(e.currentTarget).parent().parent().find($(".span2")).css("backgroundColor","green")
							$(e.currentTarget).parent().parent().find($(".span3")).text("已通")
							$(e.currentTarget).parent().parent().find($(".images")).css("backgroundColor","#1b9f94")
						}
					}
				}
			})
		}
	};
	control.init_page();
})