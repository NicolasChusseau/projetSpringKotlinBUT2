# Store Stock

Le but de ce projet est de créer un système de gestion de stock et de panier sous forme de microservices.

L'architecture micro-service permet d'isoler les parties métier les unes des autres pour qu'elles puissent évoluer plus
facilement.

Dans notre cas, il y en aura trois:

## Les articles

Cela peut être n'importe quel type d'article. Un article doit avoir au minimum:

- un ID
- un nom
- un prix
- une quantité en stock
- la date de la dernière mise à jour

Il faudra une API pour pouvoir administrer les articles (création, suppression, ajout de quantité...)

## Les utilisateurs

Vous pouvez vous inspirer de ce qui a été fait en TP jusqu'à présent. Un utilisateur doit avoir au minimum:

- un email
- un nom
- une adresse de livraison
- un champ permettant d'identifier s'ils sont abonnés à la newsletter
- la date de la dernière commande

Il faudra une API pour pouvoir administrer les utilisateurs (création de compte, suppression...)

## Les paniers

Les paniers font le lien entre les utilisateurs et les produits. Les règles sont les suivantes:

- un panier est obligatoirement lié à un utilisateur
- un utilisateur ne peut avoir qu'un seul panier
- on ne peut pas ajouter plus de quantités dans son panier s'il n'y a pas assez de stock
- on peut supprimer des éléments du panier
- à la validation du panier, si le stock n'est plus suffisant, renvoi d'un message d'erreur
- à la validation du panier, le stock est mis à jour et la date de la dernière commande est modifiée

# Contraintes

Le service panier devra proposer deux implémentations de base de données, une en mémoire et une avec une base H2.

Chaque service doit exposer un swagger-ui avec une séparation à l'aide de Tag entre les APIs d'administration (ex.
création d'un produit) et les APIs métiers (décrémentation du stock lors d'une vente).

Une couverture de test est requise pour chaque service avec ce qui vous semble le plus pertinent suivant les parties (
test unitaire, test d'intégration...).

Les logs doivent permettre de suivre les actions et les cas d'erreurs doivent être traités.
