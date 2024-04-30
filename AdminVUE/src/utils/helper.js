import moment from "moment";
export function toDateString(date) {
    return date ? moment(date).format("YYYY-MM-DD HH:mm:ss") : "-";
}