//
//  SingleSectionOrderViewController.swift
//  PointExchange
//  未使用和过期订单的tableview
//  Created by panyy on 2018/7/14.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import UIKit
import RxCocoa
import RxSwift
import RxDataSources

class CouponHistoryContainViewContainer: UIViewController {
    @IBOutlet weak var tableView: UITableView!
    
    var items = [Item]()
	
	var tag:String?
    
    // tableView设置相关
    var dataSource:RxTableViewSectionedReloadDataSource<SectionModel<String,Order>>?
    var disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.rowHeight = self.view.frame.width * 0.32 // 按比例缩放
        
		self.tableView.register(UINib(nibName: "HistoryTableViewCell", bundle: nil), forCellReuseIdentifier: "historyCell")
		if tag == "unuse" {
			ServerConnector.getUnusedCoupons(){(result, items) in
				if result {
                    self.items = items
                    if items.count == 0{
                        self.tableView.isHidden = true
                    }else{
                        self.tableView.isHidden = false
                        self.setDataSource()
                    }
					
				}
			}
		}else if tag == "used"{
			ServerConnector.getUsedCoupons(){(result, items) in
				if result {
                    self.items = items
                    if items.count == 0{
                        self.tableView.isHidden = true
                    }else{
                        self.tableView.isHidden = false
                        self.setDataSource()
                    }
				}
			}
		}
		else if tag == "overdue"{
			ServerConnector.getOverdueCoupons(){(result, items) in
				if result {
                    self.items = items
                    if items.count == 0{
                        self.tableView.isHidden = true
                    }else{
                        self.tableView.isHidden = false
                        self.setDataSource()
                    }
				}
			}
		}
    }
	
	func getDatas()->Observable<[SectionModel<String,Item>]>{
		let items = (0 ..< self.items.count).map {i in
			self.items[i]
		}
		let observable = Observable.just([SectionModel(model: "item", items: items)])
		return observable
	}
	func setDataSource(){
		let observable = Observable.empty().asObservable()
		.startWith(())
		.flatMapLatest(getDatas).share(replay: 1)
		let dataSource = RxTableViewSectionedReloadDataSource<SectionModel<String,Item>>(configureCell: { (dataSource,view,indexPath,element) in
			var cell = self.tableView.dequeueReusableCell(withIdentifier: "historyCell") as? HistoryTableViewCell
			if cell == nil {
				cell = UITableViewCell(style: .default, reuseIdentifier: "historyCell") as? HistoryTableViewCell
			}
			if self.items.count != 0 {
				let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
				self.tableView.isHidden = false
				let imageURL = URL(string: (element.logoURL?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed))!)
				cell?.logoImage.kf.setImage(with: imageURL)
				cell?.pointLabel.text = String(stringInterpolationSegment:element.points!)
				cell?.descriptionField.text = element.description!
				switch self.tag{
				case "unuse":
					cell?.backgroundImage.image = UIImage(named: "willUse")
					cell?.dateLabel.isHidden = false
					cell?.dateTitleLabel.isHidden = false
					let date = formatter.date(from: element.overdueTime!)
					formatter.dateFormat = "yyyy/MM/dd"
					cell?.dateLabel.text = formatter.string(from: date!)
					cell?.dateTitleLabel.text = "有效期至"
					cell?.invalidLabel.isHidden = true
				case "used":
					cell?.backgroundImage.image = UIImage(named: "used")
					cell?.dateLabel.isHidden = false
					cell?.dateTitleLabel.isHidden = false
					let date = formatter.date(from: element.useTime!)
					formatter.dateFormat = "yyyy/MM/dd"
					cell?.dateLabel.text = formatter.string(from: date!)
					cell?.dateTitleLabel.text = "使用日期"
					cell?.invalidLabel.isHidden = true
				case "overdue":
					cell?.backgroundImage.image = UIImage(named: "invalid")
					cell?.dateLabel.isHidden = true
					cell?.dateTitleLabel.isHidden = true
					cell?.invalidLabel.isHidden = false
				default:
					break
				}
			}else{
				self.tableView.isHidden = true
			}
			return cell!
		})
		observable.bind(to: self.tableView.rx.items(dataSource: dataSource)).disposed(by: disposeBag)
		self.tableView.rx.modelSelected(Item.self)
			.subscribe(onNext:{ value in
				let sb = UIStoryboard(name: "User", bundle: nil)
				let vc = sb.instantiateViewController(withIdentifier: "HistoryCouponDetailViewController") as! HistoryCouponDetailViewController
				vc.items = value
				self.navigationController?.pushViewController(vc, animated: true)
			}).disposed(by:disposeBag)
	}

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

}
