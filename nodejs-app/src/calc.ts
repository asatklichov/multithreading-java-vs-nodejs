export default class calc {

    static factorial( n: number ): number {
        if ( n === 0 || n === 1 )
            return 1;
        return n * this.factorial( n - 1 );
    }

    static fibonacci( n: number ): number {
        if ( n === 0 || n === 1 )
            return 1;
        return this.fibonacci( n - 2 ) + this.fibonacci( n - 1 );
    }
}

console.log("-- end --")
