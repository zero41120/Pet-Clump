
import CryptoSwift
import Foundation
import BigInt

public class KeyExchanger {
    
    var primitiveRoot: BigInt = BigInt(0)
    var bigPrime: BigInt = BigInt(0)
    var mySecret: BigInt = BigInt(0)
    let friendId: String
    private var sharedKey: Array<UInt8> = []
    
    public init (friendId: String) {
        self.friendId = friendId
        generatePublicPrimes()
        loadOrGenerateSecret(fdId: friendId)
    }
    
    public init (friendId: String, friendPublic: BigInt, bigPrime: BigInt, primitiveRoot: BigInt) {
        self.friendId = friendId
        self.bigPrime = bigPrime
        self.primitiveRoot = primitiveRoot
        loadOrGenerateSecret(fdId: friendId)
    }
    
    public init(acceptFriendId: String, data: [String : Any]){
        self.friendId = acceptFriendId
        self.bigPrime = BigInt(data["bigPrime"] as! String)!
        self.primitiveRoot = BigInt(data["priPrime"] as! String)!
        loadOrGenerateSecret(fdId: acceptFriendId)
    }
    
    /**
     * This method generates the data for the first time exchange (Add friend)
     */
    public func generateSenderData(myPetId: String, friendPetId: String) -> [String: Any]{
        return ["bigPrime" : "\(getBigPrime())",
                "priPrime" : "\(getPrimitiveRoot())",
                myPetId: "\(getMyPublic())",
                friendPetId: "?"]
    }
    
    public func getSharedKey(fdPublic: BigInt) -> Array<UInt8> {
        if sharedKey.isEmpty {
            let temp: BigInt = fdPublic.power(mySecret, modulus: bigPrime)
            let temp2: String = String(temp)
            var key: Array<UInt8> = []
            for char in temp2.utf8 {
                key += [char]
            }
            
            key = key.sha256()
            let shrkey: ArraySlice<UInt8> = key[0..<16]
            sharedKey = Array(shrkey)
        }
        print("sharedKey: \(sharedKey)")
        return sharedKey
    }
    
    private func loadOrGenerateSecret(fdId: String) {
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            let fileURL = dir.appendingPathComponent(self.friendId)

            hasSecretWithThisFriend(
                ifTrue: {
                    do {
                        let secrectString = try String(contentsOf: fileURL, encoding: .utf8)
                        self.mySecret = BigInt(secrectString)!
                        print("loaded secret: \(self.mySecret)")
                    } catch { /* TODO No time for this*/}
                }, ifFalse: {
                    do {
                        self.mySecret = BigInt(BigUInt.randomInteger(withExactWidth: 500))
                        print("generated secret: \(self.mySecret)")
                        try "\(self.mySecret)".write(to: fileURL, atomically: false, encoding: .utf8)
                    } catch { /* TODO No time for this*/}
                }
            )
        }
    }
    
    private func hasSecretWithThisFriend(ifTrue: ()->Void, ifFalse: ()->Void){
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as String
        let url = NSURL(fileURLWithPath: path)
        if let pathComponent = url.appendingPathComponent(self.friendId) {
            let filePath = pathComponent.path
            let fileManager = FileManager.default
            if fileManager.fileExists(atPath: filePath) {
                ifTrue()
            } else {
                ifFalse()
            }
        } else {
            ifFalse()
        }
    }

    public func getMyPublic() -> BigInt {
        return primitiveRoot.power(mySecret, modulus: bigPrime)
    }
    
    public func getPrimitiveRoot() -> BigInt {
        return primitiveRoot
    }
    
    public func getBigPrime() -> BigInt {
        return bigPrime
    }
    
    private func generatePublicPrimes() {
        self.bigPrime = generate512bitProbabilisticPrime()
        self.primitiveRoot = findPrimitiveRoot(p: bigPrime)
    }
    
    private func generate512bitProbabilisticPrime() -> BigInt {
        return BigInt(generatePrime(512))
    }

    private func generatePrime(_ width: Int) -> BigInt {
    while true {
        var random = BigUInt.randomInteger(withExactWidth: width)
        random |= BigUInt(1)
        if random.isPrime() {
            return BigInt(random)
            }
        }
    }
    
    //not generating array?
    private func primeFactors(number: BigInt) -> Array<BigInt> {
        var n: BigInt = number
        var i: BigInt = 2
        let limit: BigInt = 10000
        var factors: Array<BigInt> = []
        
        while n == BigInt(1) {
            while n.modulus(i).isZero {
                factors.append(i)
                n = n / i
                if n.isPrime() {
                    factors.append(n)
                    return factors
                }
            }
        }
        i = i + 1
        if i == limit {
            return factors
        }
        return factors
    }
    
    private func isPrimeRoot(g: BigInt, p: BigInt) -> Bool {
        let totient: BigInt = p - 1
        let factors: Array<BigInt> = primeFactors(number: totient)
        //var i: Int = 0
        let j: Int = factors.count
        
        for i in 0..<j {
            let factor: BigInt = factors[i]
            let t: BigInt = totient / factor
            let pow: BigInt = g.power(t,modulus: p)
            
            if pow == BigInt(1) {return false}
        }
        return true
    }
    
    private func findPrimitiveRoot(p: BigInt) -> BigInt {
        let max = 100000000
        var start = Int(arc4random()) % 50000
        while start < max{
            if(isPrimeRoot(g: BigInt(start), p: p)) {
                return BigInt(start)
            }
            start += 1
        }
        return BigInt(0)
    }
}


    
    

