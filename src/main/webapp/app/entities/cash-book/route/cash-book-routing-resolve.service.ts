import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';

@Injectable({ providedIn: 'root' })
export class CashBookRoutingResolveService implements Resolve<ICashBook | null> {
  constructor(protected service: CashBookService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashBook | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cashBook: HttpResponse<ICashBook>) => {
          if (cashBook.body) {
            return of(cashBook.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
