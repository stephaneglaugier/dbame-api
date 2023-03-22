SELECT 
    *
FROM
    voters
        LEFT JOIN
    roll ON voters.roll_id = roll.id