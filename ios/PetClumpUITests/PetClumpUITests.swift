//
//  PetClumpUITests.swift
//  PetClumpUITests
//
//  Created by Jerod Zheng on 6/3/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import XCTest
class PetClumpUITests: XCTestCase {
        
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testExample() {
        // Use recording to get started writing UI tests.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
        
        
        let app = XCUIApplication()
        app.navigationBars["Start Matching"].buttons["Setting"].tap()
        
        let aboutUsNavigationBar = app.navigationBars["About us"]
        let editButton = aboutUsNavigationBar.buttons["Edit"]
        editButton.tap()
        
        let element = app.children(matching: .window).element(boundBy: 0).children(matching: .other).element(boundBy: 2).children(matching: .other).element
        element.children(matching: .textField).element(boundBy: 0).tap()
        
        let editMeNavigationBar = app.navigationBars["Edit me"]
        let saveButton = editMeNavigationBar.buttons["Save"]
        saveButton.tap()
        editButton.tap()
        
        let textField = element.children(matching: .textField).element(boundBy: 1)
        textField.tap()
        
        let pickerWheel = app/*@START_MENU_TOKEN@*/.pickerWheels["-"]/*[[".pickers.pickerWheels[\"-\"]",".pickerWheels[\"-\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/
        pickerWheel.tap()
        saveButton.tap()
        editButton.tap()
        textField.tap()
        pickerWheel.tap()
        saveButton.tap()
        //app.staticTexts["1994/01/07"].tap()
        //app.staticTexts["Male"].tap()
        editButton.tap()
        
        let cancelButton = editMeNavigationBar.buttons["Cancel"]
        cancelButton.tap()
        editButton.tap()
        element.children(matching: .textField).element(boundBy: 2).tap()
        cancelButton.tap()
        editButton.tap()
        cancelButton.tap()
        aboutUsNavigationBar.buttons["Exit"].tap()
        
        
        /**
        
        let image = UIImage(named: "profile")
        let imageView = UIImageView(image: image)
        imageView.accessibilityLabel = "Your profile image"
        
        app.images.
        
        //app.allElementsBoundByIndex
        //app.cells.element(boundBy: 7).tap()
 
 **/
        /**
        app.images
        
        app.images["/Users/jszheng/Library/Developer/CoreSimulator/Devices/504A1467-84B3-400A-A548-78407E9E905A/data/Containers/Data/Application/B8D90FCD-E260-4683-AEA1-0BF0BFD54152/Library/Caches/https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fpetclump.appspot.com%2Fo%2Fimage%252F1c5c0bd2-74f6-4704-b27a-b1ff4d07dfc2.png%3Falt=media&token=8bf0f3f7-b984-4c62-8102-0974e9a82d83"].tap()
        app.tables/*@START_MENU_TOKEN@*/.images["/Users/jszheng/Library/Developer/CoreSimulator/Devices/504A1467-84B3-400A-A548-78407E9E905A/data/Containers/Data/Application/B8D90FCD-E260-4683-AEA1-0BF0BFD54152/Library/Caches/https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fpetclump.appspot.com%2Fo%2Fimage%252F4A1B711B-36E1-4BC9-8D9E-634BDC81D8EE.png%3Falt=media&token=5ccee99f-cbf8-48ba-a933-64940986ad8a"]/*[[".cells.images[\"\/Users\/jszheng\/Library\/Developer\/CoreSimulator\/Devices\/504A1467-84B3-400A-A548-78407E9E905A\/data\/Containers\/Data\/Application\/B8D90FCD-E260-4683-AEA1-0BF0BFD54152\/Library\/Caches\/https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fpetclump.appspot.com%2Fo%2Fimage%252F4A1B711B-36E1-4BC9-8D9E-634BDC81D8EE.png%3Falt=media&token=5ccee99f-cbf8-48ba-a933-64940986ad8a\"]",".images[\"\/Users\/jszheng\/Library\/Developer\/CoreSimulator\/Devices\/504A1467-84B3-400A-A548-78407E9E905A\/data\/Containers\/Data\/Application\/B8D90FCD-E260-4683-AEA1-0BF0BFD54152\/Library\/Caches\/https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fpetclump.appspot.com%2Fo%2Fimage%252F4A1B711B-36E1-4BC9-8D9E-634BDC81D8EE.png%3Falt=media&token=5ccee99f-cbf8-48ba-a933-64940986ad8a\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.tap()
        app.navigationBars.buttons["Back"].tap()
        app.navigationBars["Your matches"].buttons["Exit"].tap()
 **/
        
    
    }
    
}
