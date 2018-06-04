
import CryptoSwift
import Foundation
import BigInt

public class KeyExchanger {
    
    var primitiveRoot: BigInt = BigInt(0)
    var bigPrime: BigInt = BigInt(0)
    var mySecret: BigInt = BigInt(0)
    private var sharedKey: Array<UInt8> = []
    
    public init (friendId: String) {
        generatePublicPrimes()
        loadOrGenerateSecret(fdId: friendId)
    }
    
    public init (friendId: String, friendPublic: BigInt, bigPrime: BigInt, primitiveRoot: BigInt) {
        self.bigPrime = bigPrime
        self.primitiveRoot = primitiveRoot
        loadOrGenerateSecret(fdId: friendId)
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
            let shrkey: ArraySlice<UInt8> = sharedKey[0..<16]
            sharedKey = Array(shrkey)
        }
        return sharedKey
    }
    
    private func loadOrGenerateSecret(fdId: String) {
    if mySecret == BigInt(0){
        let temp: BigUInt
        temp = BigUInt.randomInteger(withExactWidth: 500)
        mySecret = BigInt(temp)
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
    
    public func generatePublicPrimes() {
        self.bigPrime = generate512bitProbabilisticPrime()
        self.primitiveRoot = findPrimitiveRoot(p: self.bigPrime)
        
    }
    
    public func generate512bitProbabilisticPrime() -> BigInt {
        return BigInt(generatePrime(20))
    }

    public func generatePrime(_ width: Int) -> BigInt {
    while true {
        var random = BigUInt.randomInteger(withExactWidth: width)
        random |= BigUInt(1)
        if random.isPrime() {
            return BigInt(random)
            }
        }
    }
    
    
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
        let factors = primeFactors(number: totient)
        //var i: Int = 0
        let j: Int = factors.count
        
        for i in 0...j {
            let factor: BigInt = factors[i]
            let t: BigInt = totient / factor
            let pow: BigInt = g.power(t,modulus: p)
            
            if pow == BigInt(1) {return false}
        }
        return true
    }
    
    public func findPrimitiveRoot(p: BigInt) -> BigInt {
        let start: Int = 2001
        for i in start...100000000 {
            if(isPrimeRoot(g: BigInt(i), p: p)) {
                return BigInt(i)
            }
        }
        return BigInt(0)
    }



}


    
    

