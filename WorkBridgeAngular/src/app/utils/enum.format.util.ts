export class EnumFormatUtil {

    static titleCase(value: string): string {

        return value
            .replace(/_/g, ' ')
            .toLowerCase()
            .replace(/\b\w/g, c => c.toUpperCase());

    }

}