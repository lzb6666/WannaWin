//
//  Order.swift
//  PointExchange
//
//  Created by panyy on 2018/7/13.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import Foundation
struct Order:Codable{
    var merchantName:String?
    var orderID:String?
    var originalPrice:Double?
    var priceAfter:Double?
    var pointsNeeded:Double?
    var userID:String?
    var state:OrderState?
    var merchantID:String?
    var time:String?
	var merchantLogoURL:String?
}
enum OrderState:String,Codable{
    case SUCCESS = "SUCCESS"
	case FAIL = "FAIL"
}
