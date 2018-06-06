//
//  Cryptographer.swift
//  PetClump
//
//  Created by YSH on 5/13/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CryptoSwift
import BigInt


// https://stackoverflow.com/questions/33342863/could-not-decrypt-a-base64string-using-cryptoswift
class Cryptographer{
    private static let KEY_BITS = 128
    
    /**
     * This is a singleton class.
     */
    private static let instance = Cryptographer()
    private init() { }
    static func getInstance() -> Cryptographer{
        return self.instance
    }
    
    /**
     * This method generates a secret key.
     * We have to make Diffie-Hellman exchange for the shared key.
     * - returns:  a byte array of the secret key
     */
    func generateSecretKey() -> Array<UInt8> {
        var keyData = Data(count: 16)
        let _ = keyData.withUnsafeMutableBytes {
            (mutableBytes: UnsafeMutablePointer<UInt8>) -> Int32 in
            SecRandomCopyBytes(kSecRandomDefault, 16, mutableBytes)
        }
        return keyData.bytes
    }
    
    
    /**
     * This method generates a initialization vector.
     * We will store this value for each message we send.
     * - returns:  a byte array of the IV
     */
    func generateInitializationVector() -> Array<UInt8>{
        return AES.randomIV(AES.blockSize)
    }
    
    /**
     * This method encrypt a UTF-8 message.
     * - parameter key: a shared secret key for encrypt
     * - parameter iv: the IV for encrypt
     * - parameter plainText: the message to encrypt
     * - returns:  a Base64 cipher text to store in the cloud
     */
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
    
    /**
     * This method decrypts a cipher text.
     * - parameter key: a shared secret key for encrypt
     * - parameter iv: the IV for encrypt
     * - parameter cipherText: a encrypted message
     * - returns:  a String of plain text
     */
    func decrypt(key: Array<UInt8>, iv: Array<UInt8>, cipherText: String) -> String {
        var plainText = "error"
        do {
            let aes = try AES(key: key, blockMode: BlockMode.CBC(iv: iv), padding: .pkcs5)
            let encryptedData = Data(base64Encoded: cipherText)
            if let encrypted = encryptedData?.bytes {
                plainText = (try String(data: Data(aes.decrypt(encrypted)), encoding: .utf8)) ?? "error"
            }
        } catch { print(error) }
        return plainText
    }
    
    func convertIV(iv: String) -> Array<UInt8> {
        var output:[UInt8] = []
        var toParse = iv
        if iv == "place_holder" { return [] }
        toParse = toParse.replacingOccurrences(of: " ", with: "", options: .literal, range: nil)
        toParse = toParse.replacingOccurrences(of: "[", with: "", options: .literal, range: nil)
        toParse = toParse.replacingOccurrences(of: "]", with: "", options: .literal, range: nil)
        let numbers = toParse.split(separator: ",")
        for number in numbers {
            output.append(UInt8(number)!)
        }
        return output
    }
}



