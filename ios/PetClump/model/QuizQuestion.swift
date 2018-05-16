//
//  QuizQuestion.swift
//  PetClump
//
//  Created by YSH on 4/30/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation

class QuizAnswer {
    public static let NO = "0"
    public static let YES = "1"
    public static let SKIP = "2"
}
class QuizQuestion {
    
    static let defaultQuestions = [
        NSLocalizedString("Do you like to answer questions?", comment: "This is a quiz question"),
        NSLocalizedString("Do you spend most of your money for your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Do you think your pet gradually looks similar to you?", comment: "This is a quiz question"),
        NSLocalizedString("Do you beat your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Have your pet ever sleepwalked?", comment: "This is a quiz question"),
        NSLocalizedString("Do you think that your pet is cute?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love flowers?", comment: "This is a quiz question"),
        NSLocalizedString("Do you talk to your pet everyday? ", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love the beach?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love to listen to music?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet make you angry sometimes? ", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet smoke weeds?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet run faster than you?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet nearsighted? ", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love to take a bath?", comment: "This is a quiz question"),
        NSLocalizedString("Can your pet be put into a jar? ", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love surfing?", comment: "This is a quiz question"),
        NSLocalizedString("Do you think your pet regard you as a servant?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet get along pretty well with other pets?", comment: "This is a quiz question"),
        NSLocalizedString("Do you take your pet outside?", comment: "This is a quiz question"),
        NSLocalizedString("Do you sing to your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet well-mannered?", comment: "This is a quiz question"),
        NSLocalizedString("Do you like short-hair animal?", comment: "This is a quiz question"),
        NSLocalizedString("Do you always feed your pet on time?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet has a dream?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet ever want to eat you?", comment: "This is a quiz question"),
        NSLocalizedString("Can you bear that when your pet bothers you?", comment: "This is a quiz question"),
        NSLocalizedString("Do you tickle your pet? ", comment: "This is a quiz question"),
        NSLocalizedString("If your pet turns into human one day, would you want to date them? ", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love to go to a park?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet bark/meow?", comment: "This is a quiz question"),
        NSLocalizedString("Can your salary easily afford your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Do you love your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet attack other pets?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love swimming?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet recognize its name when you call it?", comment: "This is a quiz question"),
        NSLocalizedString("Do you take your pet to your office or school?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love sunshine?", comment: "This is a quiz question"),
        NSLocalizedString("Do you think sterilization is fair for pets?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet randomly poo?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet pregnant? ", comment: "This is a quiz question"),
        NSLocalizedString("Do you like long-hair animal? ", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet introvert or extrovert?", comment: "This is a quiz question"),
        NSLocalizedString("Do you say goodnight to your pet before you go to sleep?", comment: "This is a quiz question"),
        NSLocalizedString("Have your pet mated before?", comment: "This is a quiz question"),
        NSLocalizedString("Do you kiss your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Do you feel energized when your pet is around you?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet wear costumes?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love can food?", comment: "This is a quiz question"),
        NSLocalizedString("Do you sleep with your pet next to you?", comment: "This is a quiz question"),
        NSLocalizedString("Have you ever wished that you and your pet can switch identity? ", comment: "This is a quiz question"),
        NSLocalizedString("Do you walk with your pet on leash?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love watching TV?", comment: "This is a quiz question"),
        NSLocalizedString("Do you cuddle with your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Do you pet different species of animals? ", comment: "This is a quiz question"),
        NSLocalizedString("Will you be distracted by your pet because your pets are too lovely?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love money?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet afraid of you? ", comment: "This is a quiz question"),
        NSLocalizedString("Have your pet done a surgery before?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet smart?", comment: "This is a quiz question"),
        NSLocalizedString("Do you want to buy more pets?", comment: "This is a quiz question"),
        NSLocalizedString("Do you prefer big pets or small pets?", comment: "This is a quiz question"),
        NSLocalizedString("Do you train your pet to do interesting activities?", comment: "This is a quiz question"),
        NSLocalizedString("Are you a vegan(if choose yes, automatically signs the person out)", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet chase other people around?", comment: "This is a quiz question"),
        NSLocalizedString("Does your pet love jungle?", comment: "This is a quiz question"),
        NSLocalizedString("Do you think about your pet when they are not with you?", comment: "This is a quiz question"),
        NSLocalizedString("Is your pet long hair?", comment: "This is a quiz question"),
        NSLocalizedString("Do you buy high quality food to your pet?", comment: "This is a quiz question"),
        NSLocalizedString("Can you bear your pet to date with other pets and make you lonely?", comment: "This is a quiz question")
    ]
    
    static func getNumberOfAvaliableQuestions() -> Int{
        return defaultQuestions.count
    }
    
    static func isAllDone(quiz: String) -> Bool {
        return (quiz.count + 5) > QuizQuestion.getNumberOfAvaliableQuestions()
    }
    
    static func  getQuestion(quizString: String, count: Int) -> [String]?{
        let beginIndex = quizString.count
        let endingIndex = beginIndex + count
        guard endingIndex < self.defaultQuestions.count + 1 else { return nil }
        let tempQuestion = Array(defaultQuestions[beginIndex ..< endingIndex])
        return tempQuestion
    }
}
