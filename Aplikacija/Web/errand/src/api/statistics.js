export const fetchStatistics = () => {
    return {
        number_of_users : 129,
        new_users: [0,0,3,5,9,3,0,1,7,10,11,6], //Od januara do decembra
        requests_created:[10,15,45,2,9,56,43,10,23,42,11,7], //Od januara do decembra
        finished_requests: [120,23], // [broj uspesno zavrsenih zahteva (status 2), broj neuspesno zavrsenih zahteva (status 3)]
        tasks_per_service: [15,20,23,10,12,5,17], //za sve serive koji postoje, rasporedjeni po id-ju u opadajucem redosledu
        userService_per_service: [5,16,2,14,20,10,9], //za sve serive koji postoje, rasporedjeni po id-ju u opadajucem redosledu
        achievement_distribution:{
            values: [0,5,10,15,20,25,30],
            no_of_users: [20,27,32,23,10,5]
        },
        users_per_grade: [2,4,6,40,27] // [1-1.5, 1.5-2.5, 2.5-3.5, 3.5-4.5, 4.5-5]
    }
}