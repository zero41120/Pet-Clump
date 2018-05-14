//
//  Cryptographer.swift
//  PetClump
//
//  Created by YSH on 5/13/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CryptoSwift
// https://stackoverflow.com/questions/33342863/could-not-decrypt-a-base64string-using-cryptoswift
class Cryptographer{
    private static let instance = Cryptographer()
    private init() { }
    
    static func getInstance() -> Cryptographer{
        return self.instance
    }
    
    private func toString(uint8Array: Array<UInt8>) -> String{
        if let goodStr = String(data: Data(uint8Array), encoding: .utf8){
            return goodStr
        }
        return uint8Array.toBase64()!
    }
    func generateSecretKey() -> Array<UInt8> {
        var keyData = Data(count: 16)
        let _ = keyData.withUnsafeMutableBytes {
            (mutableBytes: UnsafeMutablePointer<UInt8>) -> Int32 in
            SecRandomCopyBytes(kSecRandomDefault, 16, mutableBytes)
        }
        return keyData.bytes
    }
    
    func generateInitializationVector() -> Array<UInt8>{
        return AES.randomIV(AES.blockSize)
    }
    
    func encrypt(key: Array<UInt8>, iv: Array<UInt8>, plainText: String) -> String {
        var cipherText = "error"
        do {
            let aes = try AES(key: key, blockMode: BlockMode.CBC(iv: iv), padding: .pkcs5)
            let encrypted = try aes.encrypt(plainText.bytes)
            let encryptedData = Data(bytes: encrypted)
            cipherText = encryptedData.base64EncodedString()
        } catch { print(error) }
        return cipherText
    }
    
    func decrypt(key: Array<UInt8>, iv: Array<UInt8>, cipherText: String) -> String {
        var plainText = "error"
        do {
            let aes = try AES(key: key, blockMode: BlockMode.CBC(iv: iv), padding: .pkcs5)
            let encryptedData = Data(base64Encoded: cipherText)
            let encrypted = encryptedData!.bytes
            plainText = try toString(uint8Array: aes.decrypt(encrypted) )
        } catch { print(error) }
        return plainText
    }
}
